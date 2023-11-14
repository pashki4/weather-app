package com.weather.service;

import com.weather.dao.UserDao;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        User user = new User();
        user.setId(99L);
        user.setLogin("login");
        String password = "password";
        user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .build();

        Mockito.doReturn(Optional.of(user)).when(userDao).getByLoginFetch(user.getLogin());
        Mockito.doReturn(userDto).when(userMapper).map(user);
        Optional<UserDto> actualResult = userService.login(user.getLogin(), password);

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().getLogin()).isEqualTo(userDto.login);
    }

}