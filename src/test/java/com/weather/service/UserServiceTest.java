package com.weather.service;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final String PASSWORD = "password";

    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        User user = getUser("login");
        UserDto userDto = getUserDto("login");

        doReturn(Optional.of(user)).when(userDao).getByLoginFetch(user.getLogin());
        doReturn(userDto).when(userMapper).map(user);
        Optional<UserDto> actualResult = userService.login(user.getLogin(), PASSWORD);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getLogin()).isEqualTo(userDto.login);
        assertThat(actualResult.get().getId()).isEqualTo(userDto.id);
    }

    @Test
    void loginFail() {
        doReturn(Optional.empty()).when(userDao).getByLoginFetch(any());

        Optional<UserDto> actualResult = userService.login("dummy", "123");

        assertThat(actualResult).isEmpty();
        verifyNoInteractions(userMapper);
    }


    private User getUser(String login) {
        User user = new User();
        user.setId(99L);
        user.setLogin(login);
        user.setPassword(BCrypt.hashpw(PASSWORD, BCrypt.gensalt()));
        return user;
    }

    private static UserDto getUserDto(String login) {
        return UserDto.builder()
                .id(99L)
                .login(login)
                .build();
    }
}