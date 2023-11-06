package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.Session;
import com.weather.model.User;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import com.weather.util.CookiesUtil;
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
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    private static final UserMapper userMapper = new UserMapper();

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
        UserService userService = new UserService(new UserDao());
        Optional<User> optionalUser = userService.getByLogin(login);

        if (optionalUser.isPresent() && BCrypt.checkpw(req.getParameter("loginPass"), optionalUser.get().getPassword())) {
            SessionService sessionService = new SessionService(new SessionDao());
            sessionService.remove(optionalUser.get().getId());
            sessionService.saveSession(optionalUser.get());
            Optional<Session> session = sessionService.getSessionByUserId(optionalUser.get().getId());
            CookiesUtil.addCookie(resp, session.get());

            UserDto userDto = optionalUser.map(userMapper::map).get();

            if (req.getParameter("name") != null) {
                req.getRequestDispatcher("/add?userId=" + userDto.getId()).forward(req, resp);
            } else {
                userService.updateWeatherData(userDto);
                context.setVariable("user", userDto);
                templateEngine.process("authorized", context, resp.getWriter());
            }
        } else {
            ifParamsArePresentedAddToContext(req, context);
            context.setVariable("errorMessage", "Wrong credentials");
            templateEngine.process("login", context, resp.getWriter());
        }
    }

    private static void ifParamsArePresentedAddToContext(HttpServletRequest req, WebContext context) {
        if (req.getParameter("name") != null) {
            context.setVariable("name", req.getParameter("name"));
            context.setVariable("latitude", req.getParameter("latitude"));
            context.setVariable("longitude", req.getParameter("longitude"));
        }
    }
}
