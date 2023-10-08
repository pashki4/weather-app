package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDAO;
import com.weather.dao.UserDAO;
import com.weather.exception.UserDaoException;
import com.weather.model.User;
import com.weather.service.SessionService;
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

@WebServlet("/signup")
public class SignupController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        templateEngine.process("signup.jsp", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUser(req);
        UserService userService = new UserService(new UserDAO());
        SessionService sessionService = new SessionService(new SessionDAO());
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        try {
            userService.save(user);
            CookiesUtil.addCookie(resp, user);
            sessionService.saveSession(user);
            context.setVariable("user", user);
            templateEngine.process("user-data.jsp", context, resp.getWriter());
        } catch (UserDaoException e) {
            context.setVariable("errorMessage", e.getMessage());
            templateEngine.process("signup.jsp", context, resp.getWriter());
        }
    }

    private static User getUser(HttpServletRequest req) {
        User user = new User();
        user.setLogin(req.getParameter("login"));
        user.setPassword(BCrypt.hashpw(req.getParameter("pwd"), BCrypt.gensalt()));
        return user;
    }
}

