package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.AIISessionDAO;
import com.weather.dao.UserDAO;
import com.weather.model.Session;
import com.weather.model.User;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("/")
public class CheckUserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Cookie[] cookies = req.getCookies();
        boolean isSessionExpired = true;
        Long id = null;

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        if (cookies != null) {
            Optional<Cookie> userId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_id"))
                    .findAny();

            if (userId.isPresent()) {
                id = Long.parseLong(userId.get().getValue());
                SessionService sessionService = new SessionService(new AIISessionDAO());
                Optional<Session> session = sessionService.getSessionByUserId(id);
                if (session.isPresent()) {
                    isSessionExpired = checkSession(session.get());
                }
            }
        }

        if (isSessionExpired && id != null) {
            templateEngine.process("login.jsp", context, resp.getWriter());
        } else if (id == null) {
            templateEngine.process("signup.jsp", context, resp.getWriter());
        } else {
            UserService userService = new UserService(new UserDAO());
            Optional<User> user = userService.getById(id);
            context.setVariable("user", user.get());
            templateEngine.process("user-data.jsp", context, resp.getWriter());
        }
    }

    private boolean checkSession(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
