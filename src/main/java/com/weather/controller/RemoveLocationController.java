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
import java.util.Optional;

@WebServlet("/remove")
public class RemoveLocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = Long.valueOf(req.getParameter("userId"));
        Long locationId = Long.valueOf(req.getParameter("locationId"));
        LocationService locationService = new LocationService(new LocationDao());
        Location location = new Location();
        location.setId(locationId);
        locationService.remove(location);
        UserService userService = new UserService(new UserDAO());
        Optional<User> user = userService.getById(userId);

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        context.setVariable("user", user.get());
        templateEngine.process("authorized", context, resp.getWriter());
    }
}
