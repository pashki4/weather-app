package com.weather.controller;

import com.weather.dto.UserDto;
import com.weather.exception.UserDaoException;
import com.weather.model.Session;
import com.weather.model.User;
import com.weather.util.CookiesUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet("/signup")
public class SignupController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("signup", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            User user = getUser(req);
            userService.save(user);
            sessionService.saveSessionByUserId(user.getId());
            Optional<Session> session = sessionService.getSessionByUserId(user.getId());
            CookiesUtil.addCookie(resp, session.get());

            if (req.getParameter("name") != null && !req.getParameter("name").isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/add" + addParameters(req));
            } else {
                UserDto userDto = USER_MAPPER.map(user);
                req.setAttribute("user", userDto);
                processTemplate("authorized", req, resp);
            }
        } catch (RuntimeException e) {
            req.setAttribute("errorMessage", "Sorry, that login already exists!");
            processTemplate("signup", req, resp);
        }
    }

    private static User getUser(HttpServletRequest req) {
        User user = new User();
        user.setLogin(req.getParameter("signUpUserName"));
        user.setPassword(req.getParameter("signPass"));
        if (user.getLogin() == null || user.getPassword() == null) {
            throw new RuntimeException("Credentials should not be empty");
        }
        return user;
    }

    private String addParameters(HttpServletRequest req) {
        String name = URLEncoder.encode(req.getParameter("name"), StandardCharsets.UTF_8);
        return "?name=" + name
               + "&latitude=" + req.getParameter("latitude")
               + "&longitude=" + req.getParameter("longitude");
    }
}

