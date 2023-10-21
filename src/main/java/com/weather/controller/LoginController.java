package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDAO;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Session;
import com.weather.model.User;
import com.weather.service.HttpService;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import com.weather.util.CookiesUtil;
import com.weather.util.MapperUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        String login = req.getParameter("loginUserName").toLowerCase();
        UserService userService = new UserService(new UserDAO());
        Optional<User> optionalUser = userService.getByLogin(login);

        if (optionalUser.isPresent() && BCrypt.checkpw(req.getParameter("loginPass"), optionalUser.get().getPassword())) {
            SessionService sessionService = new SessionService(new SessionDAO());
            sessionService.saveSession(optionalUser.get());
            Optional<Session> session = sessionService.getSessionByUserId(optionalUser.get().getId());
            CookiesUtil.addCookie(resp, session.get());

            User user = optionalUser.get();
            UserDto userDto = MapperUtil.mapUserDto(user);

            userDto.locations.forEach(location -> {
                String latitude = String.valueOf(location.getLatitude());
                String longitude = String.valueOf(location.getLongitude());
                String weatherDataUrl = HttpService.createWeatherDataUrl(latitude, longitude);
                HttpRequest weatherDataRequest = HttpService.prepareHttpRequest(weatherDataUrl);
                location.setWeatherData(MapperUtil
                        .mapWeatherData(HttpService.sendRequest(weatherDataRequest)));
            });

            context.setVariable("user", userDto);
            templateEngine.process("authorized", context, resp.getWriter());
        } else {
            context.setVariable("errorMessage", "Wrong credentials");
            templateEngine.process("no-authorized", context, resp.getWriter());
        }
    }
}
