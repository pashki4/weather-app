package com.weather.controller;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.service.UserService;
import com.weather.service.WeatherApiService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/search")
public class SearchController extends BaseController {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Location> locations = WeatherApiService.findLocation(req);
            locations.forEach(WeatherApiService::getWeatherData);
            req.setAttribute("locations", locations);
        } catch (IOException e) {
            throw new RuntimeException("Error searching location", e);
        }

        String userId = req.getParameter("userId");
        if (userId != null) {
            long id = Long.parseLong(userId);
            UserService userService = new UserService(new UserDao());
            Optional<UserDto> userDto = userService.getById(id);
            req.setAttribute("user", userDto.get());
            processTemplate("authorized", req, resp);
        } else {
            processTemplate("no-authorized", req, resp);
        }
    }
}
