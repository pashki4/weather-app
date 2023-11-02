package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.service.UserService;
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

@WebServlet("/add")
public class AddLocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        Location location = mapLocation(req);

        if (req.getParameter("userId") == null) {
            context.setVariable("name", location.getName());
            context.setVariable("latitude", location.getLatitude());
            context.setVariable("longitude", location.getLongitude());
            templateEngine.process("login", context, resp.getWriter());
        } else {
            Long userId = Long.valueOf(req.getParameter("userId"));
            UserService userService = new UserService(new UserDAO());
            try {
                userService.addLocation(userId, location);
                UserDto userDto = userService.getById(userId).get();
                userService.updateWeatherData(userDto);
                context.setVariable("user", userDto);
                templateEngine.process("authorized", context, resp.getWriter());
            } catch (RuntimeException e) {
                UserDto userDto = userService.getById(userId).get();
                userService.updateWeatherData(userDto);
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
