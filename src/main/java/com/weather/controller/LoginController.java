package com.weather.controller;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginController extends BaseController {

    private static final UserMapper USER_MAPPER = new UserMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        processTemplate("login", req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String login = req.getParameter("loginUserName").toLowerCase();
        UserService userService = new UserService(new UserDao());
        Optional<User> optionalUser = userService.getByLogin(login);

        if (optionalUser.isPresent() && BCrypt.checkpw(req.getParameter("loginPass"), optionalUser.get().getPassword())) {
            SessionService sessionService = new SessionService(new SessionDao());
            sessionService.remove(optionalUser.get().getId());
            sessionService.saveSession(optionalUser.get());
            Optional<Session> session = sessionService.getSessionByUserId(optionalUser.get().getId());
            CookiesUtil.addCookie(resp, session.get());

            UserDto userDto = optionalUser.map(USER_MAPPER::map).get();

            if (req.getParameter("name") != null) {
                req.getRequestDispatcher("/add?userId=" + userDto.getId()).forward(req, resp);
            } else {
                userService.updateWeatherData(userDto);
                req.setAttribute("user", userDto);
                processTemplate("authorized", req, resp);
            }
        } else {
            ifParamsArePresentedAdd(req);
            req.setAttribute("errorMessage", "Wrong credentials");
            processTemplate("login", req, resp);
        }
    }

    private static void ifParamsArePresentedAdd(HttpServletRequest req) {
        if (req.getParameter("name") != null) {
            req.setAttribute("name", req.getParameter("name"));
            req.setAttribute("latitude", req.getParameter("latitude"));
            req.setAttribute("longitude", req.getParameter("longitude"));
        }
    }
}
