package com.weather.controller;

import com.weather.dao.ISessionDAO;
import com.weather.dao.IUserDAO;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        boolean isSessionExpired = true;
        if (cookies != null) {
            Optional<Cookie> userId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_id"))
                    .findAny();

            if (userId.isPresent()) {
                UserService userService = new UserService(new IUserDAO());
                Optional<User> user = userService.get(Long.parseLong(userId.get().getValue()));
                if (user.isPresent()) {
                    SessionService sessionService = new SessionService(new ISessionDAO());
                    Optional<Session> session = sessionService.getSession(user.get());
                    if (session.isPresent()) {
                        isSessionExpired = isSessionExpired(session.get());
                    }
                }
            }
        }
        if (isSessionExpired) {
            req.getRequestDispatcher("login.html").forward(req, resp);
        } else {
            req.getRequestDispatcher("authorised.html").forward(req, resp);
        }
    }

    private boolean isSessionExpired(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
