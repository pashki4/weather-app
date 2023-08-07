package com.weather.controller;

import com.weather.dao.ISessionDAO;
import com.weather.dao.IUserDAO;
import com.weather.exception.UserDaoException;
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

@WebServlet("/")
public class RegistrationController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Cookie[] cookies = req.getCookies();
        boolean isSessionExpired = true;
        Long id = null;
        if (cookies != null) {
            Optional<Cookie> userId = Arrays.stream(req.getCookies())
                    .filter(cookie -> cookie.getName().equals("user_id"))
                    .findAny();

            if (userId.isPresent()) {
                id = Long.parseLong(userId.get().getValue());
                SessionService sessionService = new SessionService(new ISessionDAO());
                Optional<Session> session = sessionService.getSessionByUserId(id);
                if (session.isPresent()) {
                    isSessionExpired = checkSession(session.get());
                }
            }
        }

        if (isSessionExpired) {
//            resp.sendRedirect("login.jsp");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        } else {
            UserService userService = new UserService(new IUserDAO());
            Optional<User> user = userService.getById(id);
            req.setAttribute("user", user.orElseThrow(() -> new UserDaoException("Cannot find user")));
            req.getRequestDispatcher("/WEB-INF/templates/authorised.jsp").forward(req, resp);
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
