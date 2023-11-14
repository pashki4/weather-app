package com.weather.service;

import com.weather.dao.IUserDao;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.Location;
import com.weather.model.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Optional;

public class UserService {
    private final IUserDao userDAO;
    private final UserMapper userMapper;

    public UserService(IUserDao userDAO, UserMapper userMapper) {
        this.userDAO = userDAO;
        this.userMapper = userMapper;
    }

    public Optional<UserDto> login(String login, String password) {
        Optional<User> user = userDAO.getByLoginFetch(login);
        if (user.isPresent() && BCrypt.checkpw(password, user.get().getPassword())) {
            return user.map(userMapper::map);
        }
        return Optional.empty();
    }

    public void save(User user) {
        userDAO.save(user);
    }

    public void addLocation(Long userId, Location location) {
        userDAO.addLocation(userId, location);
    }

    public void removeLocation(Long userId, Long locationId) {
        userDAO.removeLocation(userId, locationId);
    }

    public void updateWeatherData(UserDto userDto) {
        userDto.locations.forEach(WeatherApiService::getWeatherData);
    }
}
