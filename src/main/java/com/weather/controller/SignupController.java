package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.exception.UserDaoException;
import com.weather.model.Session;
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
import java.util.Optional;

@WebServlet("/signup")
public class SignupController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        templateEngine.process("signup", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        User user = getUser(req);
        UserService userService = new UserService(new UserDao());
        SessionService sessionService = new SessionService(new SessionDao());
        try {
            userService.save(user);
            sessionService.saveSession(user);
            Optional<Session> session = sessionService.getSessionByUserId(user.getId());
            CookiesUtil.addCookie(resp, session.get());

            if (req.getParameter("name") != null) {
                req.getRequestDispatcher("/add?userId=" + user.getId()).forward(req, resp);
            }
            context.setVariable("user", user);
            templateEngine.process("authorized", context, resp.getWriter());
        } catch (UserDaoException e) {
            context.setVariable("errorMessage", "Sorry, that login already exists!");
            templateEngine.process("signup", context, resp.getWriter());
        }
    }

    private static User getUser(HttpServletRequest req) {
        User user = new User();
        user.setLogin(req.getParameter("signUpUserName"));
        user.setPassword(BCrypt.hashpw(req.getParameter("signPass"), BCrypt.gensalt()));
        return user;
    }
}

