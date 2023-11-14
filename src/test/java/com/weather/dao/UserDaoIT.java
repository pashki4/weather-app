package com.weather.dao;

import com.weather.integration.BaseIntegrationTest;
import com.weather.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class UserDaoIT extends BaseIntegrationTest {

    private final UserDao userDao = new UserDao("h2");

    @Test
    void getByIdFetch() {

    }

    private static User getUser() {
        User user = new User();
        user.setLogin("userLogin");
        user.setPassword("userPassword");
        return user;
    }

    @Test
    void save() {
        //given
        User user = getUser();

        //when
        userDao.save(user);

        //then
        Assertions.assertThat(user.getId()).isNotNull();
    }

    @Test
    void getByLoginFetch() {
    }

    @Test
    void addLocation() {
    }

    @Test
    void removeLocation() {
    }
}