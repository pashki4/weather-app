package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.dto.WeatherData;
import com.weather.model.Location;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.weather.util.PropertiesUtil.get;

public class WeatherApiService {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String SEARCH_URL = "http://api.openweathermap.org/geo/1.0/direct";
    private static final String SEARCH_LIMIT = "10";
    private static final String WEATHER_DATA_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String WEATHER_DATA_UNITS = "metric";
    private static final String WEATHER_DATA_LANG = "en";
    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static List<Location> findLocation(HttpServletRequest req) throws JsonProcessingException {
        String searchUrl = createSearchLocationUrl(req);
        HttpRequest request = prepareGetRequest(searchUrl);
        HttpResponse<String> response = sendRequest(request);
        return mapLocation(response);
    }

    public static void getWeatherData(Location location) {
        String weatherDataUrl = WeatherApiService.createWeatherDataUrl(location);
        HttpRequest weatherDataRequest = WeatherApiService.prepareGetRequest(weatherDataUrl);
        location.setWeatherData(mapWeatherData(WeatherApiService.sendRequest(weatherDataRequest)));
    }

    private static List<Location> mapLocation(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private static WeatherData mapWeatherData(HttpResponse<String> response) {
        String body = response.body();
        try {
            JsonNode treeNode = MAPPER.readTree(body);
            String main = treeNode.get("weather").get(0).get("main").asText();
            String description = treeNode.get("weather").get(0).get("description").asText();
            String icon = treeNode.get("weather").get(0).get("icon").asText();
            Double temp = treeNode.get("main").get("temp").asDouble();
            return new WeatherData(main, description, icon, temp);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error reading json: " + body, e);
        }
    }

    private static HttpRequest prepareGetRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    private static HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error sending request: " + request, e);
        }
    }

    private static String createSearchLocationUrl(HttpServletRequest req) {
        String query = URLEncoder.encode(req.getParameter("query"), StandardCharsets.UTF_8);
        return SEARCH_URL
               + "?q=" + query
               + "&limit=" + SEARCH_LIMIT
               + "&appid=" + get("appid");
    }

    private static String createWeatherDataUrl(Location location) {
        String latitude = String.valueOf(location.getLatitude());
        String longitude = String.valueOf(location.getLongitude());
        return WEATHER_DATA_URL
               + "?lat=" + latitude
               + "&lon=" + longitude
               + "&lang=" + WEATHER_DATA_LANG
               + "&units=" + WEATHER_DATA_UNITS
               + "&appid=" + get("appid");
    }
}
