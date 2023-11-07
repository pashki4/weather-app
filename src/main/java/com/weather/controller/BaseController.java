package com.weather.controller;

import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.Session;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.IServletWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public class BaseController extends HttpServlet {

    private TemplateEngine templateEngine;

    protected SessionService sessionService = new SessionService(new SessionDao());
    protected UserService userService = new UserService(new UserDao());

    private UserMapper userMapper = new UserMapper();


    @Override
    public void init() {
        JakartaServletWebApplication application
                = JakartaServletWebApplication.buildApplication(getServletContext());
        WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    protected void processTemplate(String template, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html;charset=UTF-8");
        IServletWebExchange iServletWebExchange
                = JakartaServletWebApplication.buildApplication(getServletContext()).buildExchange(req, resp);
        WebContext webContext = new WebContext(iServletWebExchange);
        templateEngine.process(template, webContext, resp.getWriter());
    }

    protected boolean doesCookieExist(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return false;
        }
        return Arrays.stream(cookies)
                .anyMatch(c -> c.getName().equals("weather_id"));
    }

    protected boolean isSessionExpired(HttpServletRequest req) {
        UUID uuid = getCookieId(req);
        Optional<Session> session = sessionService.getSessionById(uuid);
        return session
                .map(value -> sessionService.isSessionExpired(value))
                .orElse(true);
    }

    protected Optional<UserDto> getUserBySessionId(HttpServletRequest req) {
        UUID cookieId = getCookieId(req);
        return sessionService.getSessionById(cookieId)
                .map(Session::getUser)
                .map(userMapper::map);
    }

    private UUID getCookieId(HttpServletRequest req) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals("weather_id"))
                .map(Cookie::getValue)
                .map(UUID::fromString)
                .findAny()
                .get();
    }
}
