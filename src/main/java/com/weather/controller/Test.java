package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.IUserDAO;
import com.weather.model.User;
import com.weather.service.UserService;
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

@WebServlet("/test")
public class Test extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/templates/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");

        UserService userService = new UserService(new IUserDAO());
        Optional<User> user = userService.getByLogin(login);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        if (user.isPresent()) {
            if (BCrypt.checkpw(req.getParameter("pwd"), user.get().getPassword())) {
                context.setVariable("user", user.get());
                templateEngine.process("authorised.jsp", context, resp.getWriter());
            } else {
                context.setVariable("errorMessage", "Wrong credentials");
                templateEngine.process("login.jsp", context, resp.getWriter());
            }
        } else {
            context.setVariable("errorMessage", "Wrong credentials");
            templateEngine.process("login.jsp", context, resp.getWriter());
        }
    }
}
