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

    @Mock
    private UserDao userDao;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        User user = getUser(false);
        User crypt = getUser(true);
        UserDto userDto = getUserDto();

        doReturn(Optional.of(crypt)).when(userDao).getByLoginFetch(user.getLogin());
        doReturn(userDto).when(userMapper).map(user);
        Optional<UserDto> actualResult = userService.login(user.getLogin(), user.getPassword());

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

    private static User getUser(Boolean cryptPass) {
        User user = new User();
        user.setId(99L);
        user.setLogin("login");
        cryptPassword(cryptPass, user);
        return user;
    }

    private static void cryptPassword(Boolean crypt, User user) {
        if (crypt) {
            user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        } else {
            user.setPassword("password");
        }
    }

    private static UserDto getUserDto() {
        return UserDto.builder()
                .id(99L)
                .login("login")
                .build();
    }
}