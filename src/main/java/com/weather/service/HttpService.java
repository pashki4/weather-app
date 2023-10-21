package com.weather.service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.weather.util.PropertiesUtil.get;

public class HttpService {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpRequest prepareHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    public static String createSearchLocationUrl(HttpServletRequest req) {
        String city = req.getParameter("city").replace(" ", "+");
        return get("search.url") + city + "&limit=" + get("search.limit") + "&appid=" + get("appid");
    }

    public static String createWeatherDataUrl(String latitude, String longitude) {
        return get("weather-data.url") + "lat=" + latitude + "&lon=" + longitude + "&lang=" + get("weather-data.lang")
               + "&units=" + get("weather-data.units") + "&appid=" + get("appid");
    }

    public static HttpResponse<String> sendRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error sending request: " + request, e);
        }
    }
}
