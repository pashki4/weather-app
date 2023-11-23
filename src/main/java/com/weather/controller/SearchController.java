package com.weather.controller;

import com.weather.dto.UserDto;
import com.weather.model.Location;
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

        if (hasCookie(req) && isSessionActive(req)) {
            Optional<UserDto> user = getUserBySessionId(req);
            if (user.isPresent()) {
                user.ifPresent(userService::updateWeatherData);
                req.setAttribute("user", user.get());
                processTemplate("authorized", req, resp);
            } else {
                processTemplate("no-authorized", req, resp);
            }
        } else {
            processTemplate("no-authorized", req, resp);
        }
    }
}
