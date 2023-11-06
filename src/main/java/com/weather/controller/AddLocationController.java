package com.weather.controller;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/add")
public class AddLocationController extends BaseController {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Location location = mapLocation(req);

        if (req.getParameter("userId") == null) {
            req.setAttribute("name", location.getName());
            req.setAttribute("latitude", location.getLatitude());
            req.setAttribute("longitude", location.getLongitude());
            processTemplate("login", req, resp);
        } else {
            Long userId = Long.valueOf(req.getParameter("userId"));
            UserService userService = new UserService(new UserDao());
            try {
                userService.addLocation(userId, location);
                UserDto userDto = userService.getById(userId).get();
                userService.updateWeatherData(userDto);
                req.setAttribute("user", userDto);
                processTemplate("authorized", req, resp);
            } catch (RuntimeException e) {
                UserDto userDto = userService.getById(userId).get();
                userService.updateWeatherData(userDto);
                req.setAttribute("user", userDto);
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
