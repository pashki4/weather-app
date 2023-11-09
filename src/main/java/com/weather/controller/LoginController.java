package com.weather.controller;

import com.weather.dto.UserDto;
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

@WebServlet("/login")
public class LoginController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<User> optionalUser = USER_SERVICE.getByLogin(req.getParameter("loginUserName").toLowerCase());
        if (optionalUser.isPresent()
            && BCrypt.checkpw(req.getParameter("loginPass"), optionalUser.get().getPassword())) {
            Long userId = optionalUser.get().getId();
            SESSION_SERVICE.remove(userId);

            SESSION_SERVICE.saveSession(optionalUser.get());
            Optional<Session> session = SESSION_SERVICE.getSessionByUserId(userId);
            session.ifPresent(s -> CookiesUtil.addCookie(resp, s));

            UserDto userDto = optionalUser.map(USER_MAPPER::map).orElseThrow();
            if (req.getParameter("name") != null && !req.getParameter("name").isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/add" + addParameters(req));
            } else {
                USER_SERVICE.updateWeatherData(userDto);
                req.setAttribute("user", userDto);
                processTemplate("authorized", req, resp);
            }
        } else {
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
