package com.weather.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.model.Location;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.weather.util.PropertiesUtil.*;

@WebServlet(urlPatterns = "/search")
public class SearchCityController extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = createUrl(req);
        HttpRequest request = createHttpRequest(url);
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Location> locations = mapLocationsFromResponse(response);

    }

    private static List<Location> mapLocationsFromResponse(HttpResponse<String> response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper.readValue(response.body(), new TypeReference<>() {
        });
    }

    private static HttpRequest createHttpRequest(String url) {
        return HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(URI.create(url))
                .build();
    }

    private static String createUrl(HttpServletRequest req) {
        String city = req.getParameter("city").replace(" ", "+");
        return get("apiUrl") + city + "&limit=" + get("limit") + "&appid=" + get("appid");
    }
}
