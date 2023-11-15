package com.weather.controller;

import com.weather.dto.UserDto;
import com.weather.exception.UserDaoException;
import com.weather.model.Session;
import com.weather.util.CookiesUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("loginUserName").toLowerCase();
        String pass = req.getParameter("loginPass");
        try {
            Optional<UserDto> user = userService.login(login, pass);
            if (user.isPresent()) {
                sessionService.updateSessionByUserId(user.get().getId());
                Optional<Session> session = sessionService.getSessionByUserId(user.get().getId());
                session.ifPresent(s -> CookiesUtil.addCookie(resp, s));

                if (req.getParameter("name") != null && !req.getParameter("name").isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/add" + addParameters(req));
                } else {
                    userService.updateWeatherData(user.get());
                    req.setAttribute("user", user.get());
                    processTemplate("authorized", req, resp);
                }
            } else {
                req.setAttribute("errorMessage", "Wrong credentials");
                processTemplate("login", req, resp);
            }
        } catch (UserDaoException e) {
            req.setAttribute("errorMessage", "Wrong credentials");
            processTemplate("login", req, resp);
        }
    }

    private String addParameters(HttpServletRequest req) {
        String name = URLEncoder.encode(req.getParameter("name"), StandardCharsets.UTF_8);
        return "?name=" + name
               + "&latitude=" + req.getParameter("latitude")
               + "&longitude=" + req.getParameter("longitude");
    }
}
