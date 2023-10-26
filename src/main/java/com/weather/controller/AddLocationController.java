package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.model.User;
import com.weather.service.HttpService;
import com.weather.service.UserService;
import com.weather.util.MapperUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.Optional;

@WebServlet("/add")
public class AddLocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        if (req.getParameter("userId") == null) {
            Location location = mapLocation(req);
            context.setVariable("location", location);
            templateEngine.process("login", context, resp.getWriter());
        } else {
            Long userId = Long.valueOf(req.getParameter("userId"));
            Location location = mapLocation(req);
            UserService userService = new UserService(new UserDAO());
            try {
                userService.addLocation(userId, location);
                Optional<User> optionalUser = userService.getById(userId);
                UserDto userDto = MapperUtil.mapUserDto(optionalUser.get());
                userDto.locations.forEach(MapperUtil::updateWeatherData);
                context.setVariable("user", userDto);
                templateEngine.process("authorized", context, resp.getWriter());
            } catch (RuntimeException e) {
                Optional<User> optionalUser = userService.getById(userId);
                UserDto userDto = MapperUtil.mapUserDto(optionalUser.get());
                userDto.locations.forEach(MapperUtil::updateWeatherData);
                context.setVariable("user", userDto);
                templateEngine.process("authorized", context, resp.getWriter());
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
