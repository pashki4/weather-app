package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.service.WeatherApiService;
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
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/search")
public class SearchController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        try {
            List<Location> locations = WeatherApiService.findLocation(req);
            locations.forEach(WeatherApiService::getWeatherData);
            context.setVariable("locations", locations);
        } catch (IOException e) {
            throw new RuntimeException("Error searching location", e);
        }

        String userId = req.getParameter("userId");
        if (userId != null) {
            long id = Long.parseLong(userId);
            UserService userService = new UserService(new UserDAO());
            Optional<UserDto> userDto = userService.getById(id);
            context.setVariable("user", userDto.get());
            templateEngine.process("authorized", context, resp.getWriter());
        } else {
            templateEngine.process("no-authorized", context, resp.getWriter());
        }
    }
}
