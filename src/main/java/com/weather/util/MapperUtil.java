package com.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.dto.UserDto;
import com.weather.dto.WeatherData;
import com.weather.model.Location;
import com.weather.model.User;

import java.net.http.HttpResponse;
import java.util.List;

public class MapperUtil {

    public static WeatherData mapWeatherData(HttpResponse<String> response) {
        String body = response.body();
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode treeNode = mapper.readTree(body);
            String main = treeNode.get("weather").get(0).get("main").asText();
            String description = treeNode.get("weather").get(0).get("description").asText();
            String icon = treeNode.get("weather").get(0).get("icon").asText();
            Double temp = treeNode.get("main").get("temp").asDouble();
            return new WeatherData(main, description, icon, temp);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading json: " + body, e);
        }
    }

    public static UserDto mapUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.login = user.getLogin();
        userDto.locations = user.getLocations();
        return userDto;
    }

    public static List<Location> mapLocation(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }


}
