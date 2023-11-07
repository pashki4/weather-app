package com.weather.controller;

import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.model.Session;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/")
public class CheckUserController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (doesCookieExist(req) && !isSessionExpired(req)) {
            Optional<UserDto> user = getUserBySessionId(req);
            user.ifPresent(userService::updateWeatherData);
            if (user.isPresent()) {
                req.setAttribute("user", user.get());
                processTemplate("authorized", req, resp);
            }
        } else {
            processTemplate("no-authorized", req, resp);
        }
    }
}
