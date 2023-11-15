package com.weather.dao;

import com.weather.exception.SessionDaoException;
import com.weather.integration.BaseIntegrationTest;
import com.weather.model.Session;
import com.weather.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class SessionDaoIT extends BaseIntegrationTest {

    private final ISessionDao sessionDao = new SessionDao("h2");
    private final IUserDao userDao = new UserDao("h2");

    @Test
    void getSessionByUserId() {
        User user = getUser();
        userDao.save(user);
        sessionDao.saveByUserId(user.getId());

        Optional<Session> actualResult = sessionDao.getSessionByUserId(user.getId());

        assertThat(actualResult).isPresent();
    }

    @Test
    void shouldNotGetSessionByUserId() {
        Long fakeUserId = 99L;
        Assertions.assertThatThrownBy(
                () -> sessionDao.saveByUserId(fakeUserId))
                .isInstanceOf(SessionDaoException.class)
                .hasMessage("Cannot perform saveByUserId(" + fakeUserId + ")");
    }

    @Test
    void saveByUserId() {
        User user = getUser();
        userDao.save(user);

        sessionDao.saveByUserId(user.getId());

        Optional<Session> actualResult = sessionDao.getSessionByUserId(user.getId());
        assertThat(actualResult).isPresent();
    }

    @Test
    void getSessionById() {
        User user = getUser();
        userDao.save(user);
        sessionDao.saveByUserId(user.getId());
        Optional<Session> session = sessionDao.getSessionByUserId(user.getId());
        UUID sessionId = session.get().getId();

        Optional<Session> actualResult = sessionDao.getSessionById(sessionId);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(session.get());
    }

    @Test
    void shouldNotGetSessionById() {
        UUID fakeUUID = UUID.randomUUID();

        Optional<Session> actualResult = sessionDao.getSessionById(fakeUUID);

        assertThat(actualResult).isNotPresent();
    }

    @Test
    void removeByUserId() {
        User user = getUser();
        userDao.save(user);
        sessionDao.saveByUserId(user.getId());

        sessionDao.removeByUserId(user.getId());

        Optional<Session> actualResult = sessionDao.getSessionByUserId(user.getId());
        assertThat(actualResult).isNotPresent();
    }
}