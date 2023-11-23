package com.weather.controller;

import com.weather.dto.UserDto;
import com.weather.model.Location;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/add")
public class AddLocationController extends BaseController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Location location = mapLocation(req);

        if (!hasCookie(req) || !isSessionActive(req)) {
            req.setAttribute("name", location.getName());
            req.setAttribute("latitude", location.getLatitude());
            req.setAttribute("longitude", location.getLongitude());
            processTemplate("login", req, resp);
        } else {
            Optional<UserDto> user = getUserBySessionId(req);
            try {
                userService.addLocation(user.get().getId(), location);
                user.ifPresent(u -> u.getLocations().add(location));
                user.ifPresent(userService::updateWeatherData);
                req.setAttribute("user", user.get());
                processTemplate("authorized", req, resp);
            } catch (RuntimeException e) {
                userService.updateWeatherData(user.get());
                req.setAttribute("user", user.get());
                processTemplate("authorized", req, resp);
            }
        }
    }

    private Location mapLocation(HttpServletRequest req) {
        Location location = new Location();
        location.setName(req.getParameter("name"));
        location.setLongitude(new BigDecimal(req.getParameter("longitude")));
        location.setLatitude(new BigDecimal(req.getParameter("latitude")));
        return location;
    }
}
