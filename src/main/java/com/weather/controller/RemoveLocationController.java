package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.model.User;
import com.weather.service.HttpService;
import com.weather.service.UserService;
import com.weather.util.MapperUtil;
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
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

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

        UserService userService = new UserService(new UserDAO());
        userService.removeLocation(userId, locationId);
        Optional<User> optionalUser = userService.getById(userId);
        User user = optionalUser.get();
        UserDto userDto = MapperUtil.mapUserDto(user);
        userDto.locations.forEach(MapperUtil::updateWeatherData);

        context.setVariable("user", userDto);
        templateEngine.process("authorized", context, resp.getWriter());
    }
}
