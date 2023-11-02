package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDAO;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Session;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/")
public class CheckUserController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        Cookie[] cookies = req.getCookies();
        boolean isSessionExpired = true;
        UUID uuid = null;

        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);

        Optional<Session> session = Optional.empty();
        if (cookies != null) {
            Optional<Cookie> weatherId = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals("weather_id"))
                    .findAny();

            if (weatherId.isPresent()) {
                uuid = UUID.fromString(weatherId.get().getValue());
                SessionService sessionService = new SessionService(new SessionDAO());
                 session = sessionService.getSessionById(uuid);
                if (session.isPresent()) {
                    isSessionExpired = checkSession(session.get());
                }
            }
        }

        if (uuid == null || isSessionExpired) {
            templateEngine.process("no-authorized", context, resp.getWriter());
        } else {
            UserService userService = new UserService(new UserDAO());
            UserDto userDto = userService.getById(session.get().getUser().getId()).get();
            userService.updateWeatherData(userDto);

            context.setVariable("user", userDto);
            templateEngine.process("authorized", context, resp.getWriter());
        }
    }

    private boolean checkSession(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now(ZoneId.of("UTC")));
    }
}
