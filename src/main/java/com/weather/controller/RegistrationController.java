package com.weather.controller;

import com.weather.dao.ISessionDAO;
import com.weather.dao.IUserDAO;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@WebServlet("")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Cookie> userId = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("user_id"))
                .findAny();

        boolean isSessionExpired = true;
        if (userId.isPresent()) {
            UserService userService = new UserService(new IUserDAO());
            Optional<User> user = userService.get(Long.parseLong(userId.get().getValue()));
            SessionService sessionService = new SessionService(new ISessionDAO());
            isSessionExpired = sessionService.isSessionExpired(user.get());
        }
        if (isSessionExpired) {
            req.getRequestDispatcher("unauthorised.html").forward(req, resp);
        } else {
            req.getRequestDispatcher("authorised.html").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
