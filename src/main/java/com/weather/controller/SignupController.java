package com.weather.controller;

import com.weather.dao.SessionDao;
import com.weather.dao.UserDao;
import com.weather.exception.UserDaoException;
import com.weather.model.Session;
import com.weather.model.User;
import com.weather.service.SessionService;
import com.weather.service.UserService;
import com.weather.util.CookiesUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/signup")
public class SignupController extends BaseController {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("signup", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        User user = getUser(req);
        UserService userService = new UserService(new UserDao());
        SessionService sessionService = new SessionService(new SessionDao());
        try {
            userService.save(user);
            sessionService.saveSession(user);
            Optional<Session> session = sessionService.getSessionByUserId(user.getId());
            CookiesUtil.addCookie(resp, session.get());

            if (req.getParameter("name") != null) {
                req.getRequestDispatcher("/add?userId=" + user.getId()).forward(req, resp);
            }
            req.setAttribute("user", user);
            processTemplate("authorized", req, resp);
        } catch (UserDaoException e) {
            req.setAttribute("errorMessage", "Sorry, that login already exists!");
            processTemplate("signup", req, resp);
        }
    }

    private static User getUser(HttpServletRequest req) {
        User user = new User();
        user.setLogin(req.getParameter("signUpUserName"));
        user.setPassword(BCrypt.hashpw(req.getParameter("signPass"), BCrypt.gensalt()));
        return user;
    }
}

