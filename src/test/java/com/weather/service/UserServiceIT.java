package com.weather.service;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.exception.UserDaoException;
import com.weather.integration.IntegrationTestBase;
import com.weather.mapper.UserMapper;
import com.weather.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserServiceIT extends IntegrationTestBase {

    private UserService userService;

    @BeforeEach
    void setup() {
        userService = new UserService(new UserDao("h2"), new UserMapper());
    }

    @Test
    void login() {
        User user = getUser();
        userService.save(user);

        Optional<UserDto> actualResult = userService.login(user.getLogin(), getUser().getPassword());

        assertThat(actualResult).isPresent();
    }

    @Test
    void shouldNotLoginWrongLogin() {
        User user = getUser();
        userService.save(user);

        assertThatThrownBy(() -> userService.login("wrongLogin", user.getPassword()))
                .isInstanceOf(UserDaoException.class);
    }

    @Test
    void shouldNotLoginWrongPassword() {
        User user = getUser();
        userService.save(user);

        Optional<UserDto> actualResult = userService.login(user.getLogin(), "actualResult");

        assertThat(actualResult).isNotPresent();
    }
}