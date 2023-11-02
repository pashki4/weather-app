package com.weather.service;

import com.weather.dao.IUserDAO;
import com.weather.dto.UserDto;
import com.weather.mapper.UserMapper;
import com.weather.model.Location;
import com.weather.model.User;

import java.util.Optional;

public class UserService {
    private final IUserDAO userDAO;
    private final UserMapper userMapper = new UserMapper();

    public UserService(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<UserDto> getById(Long id) {
        return userDAO.getByIdFetch(id)
                .map(userMapper::map);
    }

    public Optional<User> getByLogin(String login) {
        return userDAO.getByLoginFetch(login);
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
