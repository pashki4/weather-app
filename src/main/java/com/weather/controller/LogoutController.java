package com.weather.controller;

import com.weather.config.ThymeleafConfiguration;
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
import java.util.Arrays;

@WebServlet("/logout")
public class Logout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("user_id"))
                .findAny()
                .ifPresent(cookie -> {
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                });
        TemplateEngine templateEngine = (TemplateEngine) getServletContext().getAttribute(
                ThymeleafConfiguration.TEMPLATE_ENGINE_ATTR);
        IWebExchange webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext context = new WebContext(webExchange);
        templateEngine.process("login.jsp", context, resp.getWriter());
    }
}
