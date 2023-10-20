package com.weather.dto.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.dto.UserDto;
import com.weather.dto.WeatherData;
import com.weather.model.User;

public class DtoMapper {

    public static WeatherData mapWeatherData(String resource) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(resource);
        String main = jsonNode.get("weather").get(0).get("main").asText();
        String description = jsonNode.get("weather").get(0).get("description").asText();
        String icon = jsonNode.get("weather").get(0).get("icon").asText();
        Double temp = jsonNode.get("main").get("temp").asDouble();
        return new WeatherData(main, description, icon, temp);
    }

    public static UserDto mapUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.login = user.getLogin();
        userDto.locations = user.getLocations();
        return userDto;
    }
}
