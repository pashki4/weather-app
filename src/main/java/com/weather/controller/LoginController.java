package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDAO;
import com.weather.model.User;
import com.weather.service.UserService;
import com.weather.util.CookiesUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        UserService userService = new UserService(new UserDAO());
        Optional<User> user = userService.getByLogin(login);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        if (user.isPresent() && BCrypt.checkpw(req.getParameter("pwd"), user.get().getPassword())) {
            CookiesUtil.addCookie(resp, user.get());
            context.setVariable("user", user.get());
            templateEngine.process("authorized", context, resp.getWriter());
        } else {
            context.setVariable("errorMessage", "Wrong credentials");
            templateEngine.process("no-authorized", context, resp.getWriter());
        }
    }
}
