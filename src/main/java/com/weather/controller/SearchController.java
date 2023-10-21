package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.IUserDAO;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
import com.weather.model.User;
import com.weather.service.HttpService;
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
import java.net.http.HttpRequest;
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

        String searchUrl = HttpService.createSearchLocationUrl(req);
        HttpRequest searchRequest = HttpService.prepareHttpRequest(searchUrl);

        try {
            List<Location> locations = MapperUtil.mapLocation(HttpService.sendRequest(searchRequest));
            locations.forEach(location -> {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                String weatherDataUrl = HttpService.createWeatherDataUrl(latitude, longitude);
                HttpRequest weatherDataRequest = HttpService.prepareHttpRequest(weatherDataUrl);
                location.setWeatherData(MapperUtil
                        .mapWeatherData(HttpService.sendRequest(weatherDataRequest)));
            });
            context.setVariable("locations", locations);
        } catch (IOException e) {
            throw new RuntimeException("Error sending request to: " + searchUrl, e);
        }

        String userId = req.getParameter("userId");
        if (userId != null) {
            long id = Long.parseLong(userId);
            IUserDAO userDAO = new UserDAO();
            Optional<User> optionalUser = userDAO.getByIdFetch(id);
            User user = optionalUser.get();
            UserDto userDto = MapperUtil.mapUserDto(user);
            context.setVariable("user", userDto);
            templateEngine.process("authorized", context, resp.getWriter());
        } else {
            templateEngine.process("no-authorized", context, resp.getWriter());
        }
    }
}
