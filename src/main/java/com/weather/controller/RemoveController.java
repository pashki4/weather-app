package com.weather.controller;

import com.weather.dto.UserDto;
import com.weather.model.Location;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/remove")
public class RemoveController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (hasCookie(req) && isSessionActive(req)) {
            Long locationId = Long.valueOf(req.getParameter("locationId"));
            Optional<UserDto> user = getUserBySessionId(req);
            if (user.isPresent()) {
                userService.removeLocation(user.get().getId(), locationId);
                user.get().locations.remove(new Location(locationId));
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
