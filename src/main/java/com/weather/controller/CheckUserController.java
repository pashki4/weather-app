package com.weather.controller;

import com.weather.dto.UserDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/")
public class CheckUserController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (hasCookie(req) && isSessionActive(req)) {
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
