package com.weather.dao;

import com.weather.exception.UserDaoException;
import com.weather.integration.IntegrationTestBase;
import com.weather.model.Location;
import com.weather.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserDaoIntegrationTest extends IntegrationTestBase {

    private final IUserDao userDao = new UserDao("h2");

    @Test
    void getByIdFetch() {
        User user = getUser();
        Location location = getLocation();
        userDao.save(user);
        userDao.addLocation(user.getId(), location);

        Optional<User> actualResult = userDao.getByIdFetch(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getLogin()).isEqualTo(user.getLogin());
        assertThat(actualResult.get().getLocations()).contains(location);
    }

    @Test
    void shouldNotGetByIdFetch() {
        User user = getUser();
        userDao.save(user);

        Long notUserId = user.getId() + 1;

        assertThatThrownBy(() -> userDao.getByIdFetch(notUserId))
                .isInstanceOf(UserDaoException.class)
                .hasMessage("Error performing getByIdFetch(" + notUserId + ")");
    }

    @Test
    void save() {
        User user = getUser();

        userDao.save(user);

        assertThat(user.getId()).isNotNull();
    }

    @Test
    void shouldNotSaveSameUser() {
        User user1 = getUser();
        User user2 = getUser();
        userDao.save(user1);

        assertThatThrownBy(() -> userDao.save(user2))
                .isInstanceOf(UserDaoException.class)
                .hasMessage("Error performing save(" + user2 + ")");
    }

    @Test
    void getByLoginFetch() {
        User user = getUser();
        Location location = getLocation();
        userDao.save(user);
        userDao.addLocation(user.getId(), location);

        Optional<User> actualResult = userDao.getByLoginFetch(user.getLogin());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getLogin()).isEqualTo(user.getLogin());
        assertThat(actualResult.get().getLocations()).contains(location);
    }

    @Test
    void shouldNotGetByLogin() {
        User user = getUser();
        userDao.save(user);

        assertThatThrownBy(() -> userDao.getByLoginFetch("dummy"))
                .isInstanceOf(UserDaoException.class)
                .hasMessage("Error performing getByLoginFetch(dummy)");
    }

    @Test
    void addLocation() {
        User user = getUser();
        Location location = getLocation();
        userDao.save(user);

        userDao.addLocation(user.getId(), location);

        List<Location> actualResult = userDao.getByIdFetch(user.getId()).get().getLocations();
        assertThat(actualResult).size().isEqualTo(1);
        assertThat(actualResult).contains(location);
    }

    @Test
    void shouldNotAddLocation() {
        User user = getUser();
        Location location1 = getLocation();
        Location location2 = getLocation();
        userDao.save(user);
        userDao.addLocation(user.getId(), location1);

        assertThatThrownBy(() -> userDao.addLocation(user.getId(), location2))
                .isInstanceOf(UserDaoException.class);
    }

    @Test
    void removeLocation() {
        User user = getUser();
        Location location = getLocation();
        userDao.save(user);
        userDao.addLocation(user.getId(), location);

        userDao.removeLocation(user.getId(), location.getId());
        Optional<User> actualResult = userDao.getByIdFetch(user.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getLocations()).hasSize(0);
    }

    @Test
    void shouldNotRemoveLocation() {
        User user = getUser();
        Location location = getLocation();
        userDao.save(user);
        userDao.addLocation(user.getId(), location);

        assertThatThrownBy(() -> userDao.removeLocation(user.getId(), 99L))
                .isInstanceOf(UserDaoException.class);
    }
}