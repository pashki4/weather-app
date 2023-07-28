package com.weather.controller;

import com.weather.dao.ISessionDAO;
import com.weather.model.Session;
import com.weather.service.SessionService;
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
        Optional<Cookie> sessionId = Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("session_id"))
                .findAny();

        UUID id = null;
        if (sessionId.isPresent()) {
            id = UUID.fromString(sessionId.get().getValue());
        }
        if (id != null && !isSessionExpired(id)) {
            req.getRequestDispatcher("authorised.html").forward(req, resp);
        } else {
            req.getRequestDispatcher("unauthorised.html").forward(req, resp);
        }
    }

    private boolean isSessionExpired(UUID uuid) {
        SessionService sessionService = new SessionService(new ISessionDAO());
        Optional<Session> session = sessionService.findSessionById(uuid);

        if (session.isEmpty()) {
            return true;
        } else {
            return sessionService.isSessionExpired(uuid);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
