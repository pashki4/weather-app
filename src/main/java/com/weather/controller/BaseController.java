package com.weather.controller;

import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.service.SessionService;
import com.weather.service.UserService;
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

public class BaseController extends HttpServlet {

    private TemplateEngine templateEngine;

    protected SessionService sessionService = new SessionService(new SessionDao());
    protected UserService userService = new UserService(new UserDao());


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

    protected boolean isThereTheCookie(HttpServletRequest req) {
        return Arrays.stream(req.getCookies())
                .anyMatch(c -> c.getName().equals("weather_id"));
    }
}
