package com.weather.util;

import com.weather.model.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    private CookieUtil() {
        throw new IllegalArgumentException("Util class");
    }

    public static void addCookie(HttpServletResponse resp, User user) {
        Cookie cookie = new Cookie("user_id", String.valueOf(user.getId()));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(120);
        resp.addCookie(cookie);
    }

}
