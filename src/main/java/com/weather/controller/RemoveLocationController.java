package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
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
import java.util.Optional;

@WebServlet("/remove")
public class RemoveLocationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        Long userId = Long.valueOf(req.getParameter("userId"));
        Long locationId = Long.valueOf(req.getParameter("locationId"));

        UserService userService = new UserService(new UserDao());
        userService.removeLocation(userId, locationId);
        Optional<UserDto> userDto = userService.getById(userId);
        userDto.ifPresent(userService::updateWeatherData);

        context.setVariable("user", userDto.get());
        templateEngine.process("authorized", context, resp.getWriter());
    }
}
