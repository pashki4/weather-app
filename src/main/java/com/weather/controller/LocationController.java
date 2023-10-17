package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.LocationDao;
import com.weather.dao.UserDAO;
import com.weather.model.Location;
import com.weather.model.User;
import com.weather.service.LocationService;
import com.weather.service.UserService;
import jakarta.servlet.ServletException;
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
import java.util.Optional;

@WebServlet("/add")
public class LocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        Location location = mapLocation(req);

        UserService userService = new UserService(new UserDAO());
        Optional<User> user = userService.getById(userId);
        user.get().addLocation(location);

        LocationService locationService = new LocationService(new LocationDao());
        locationService.add(location);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        context.setVariable("user", user.get());
        templateEngine.process("authorized", context, resp.getWriter());
    }

    private Location mapLocation(HttpServletRequest req) {
        Location location = new Location();
        location.setName(req.getParameter("name"));
        location.setLongitude(new BigDecimal(req.getParameter("longitude")));
        location.setLatitude(new BigDecimal(req.getParameter("latitude")));
        return location;
    }
}
