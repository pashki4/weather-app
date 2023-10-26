package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDAO;
import com.weather.dao.UserDAO;
import com.weather.dto.UserDto;
import com.weather.model.Location;
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
import java.math.BigDecimal;
import java.net.http.HttpRequest;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        templateEngine.process("login", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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

            if (req.getParameter("name") != null) {
                req.getRequestDispatcher("/add?userId=" + user.getId()).forward(req, resp);
            } else {
                userDto.locations.forEach(MapperUtil::updateWeatherData);
                context.setVariable("user", userDto);
                templateEngine.process("authorized", context, resp.getWriter());
            }
        } else {
            ifLocationIsPresentedAddToContext(req, context);
            context.setVariable("errorMessage", "Wrong credentials");
            templateEngine.process("login", context, resp.getWriter());
        }
    }

    private static void ifLocationIsPresentedAddToContext(HttpServletRequest req, WebContext context) {
        if (req.getParameter("name") != null) {
            Location location = new Location();
            location.setName(req.getParameter("name"));
            location.setLatitude(new BigDecimal(req.getParameter("latitude")));
            location.setLongitude(new BigDecimal(req.getParameter("longitude")));
            context.setVariable("location", location);
        }
    }
}
