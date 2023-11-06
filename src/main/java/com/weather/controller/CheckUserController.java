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
        Cookie[] cookies = req.getCookies();
        boolean isSessionExpired = true;
        UUID uuid = null;

        Optional<Session> session = Optional.empty();
        if (cookies != null) {
            Optional<Cookie> weatherId = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("weather_id"))
                    .findAny();

            if (weatherId.isPresent()) {
                uuid = UUID.fromString(weatherId.get().getValue());
                SessionService sessionService = new SessionService(new SessionDao());
                session = sessionService.getSessionById(uuid);
                if (session.isPresent()) {
                    isSessionExpired = sessionService.isSessionExpired(session.get());
                }
            }
        }

        if (uuid == null || isSessionExpired) {
            processTemplate("no-authorized", req, resp);
        } else {
            UserService userService = new UserService(new UserDao());
            UserDto userDto = userService.getById(session.get().getUser().getId()).get();
            userService.updateWeatherData(userDto);

            req.setAttribute("user", userDto);
            processTemplate("authorized", req, resp);
        }
    }
}
