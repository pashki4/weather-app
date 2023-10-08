package com.weather.util;

import com.weather.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

public class CookiesUtil {
    private CookiesUtil() {
        throw new IllegalArgumentException("Util class");
    }

    public static void addCookie(HttpServletResponse resp, User user) {
        Cookie cookie = new Cookie("user_id", String.valueOf(user.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(120);
        resp.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest req, HttpServletResponse resp) {
        Arrays.stream(req.getCookies())
                .filter(cookie -> cookie.getName().equals("user_id"))
                .findAny()
                .ifPresent(cookie -> {
                    cookie.setHttpOnly(true);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    resp.addCookie(cookie);
                });
    }
}
