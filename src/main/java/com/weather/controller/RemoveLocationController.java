package com.weather.controller;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/remove")
public class RemoveLocationController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long locationId = Long.valueOf(req.getParameter("locationId"));

        UserService userService = new UserService(new UserDao());
        userService.removeLocation(userId, locationId);
        Optional<UserDto> userDto = userService.getById(userId);
        userDto.ifPresent(userService::updateWeatherData);

        req.setAttribute("user", userDto.get());
        processTemplate("authorized", req, resp);
    }
}
