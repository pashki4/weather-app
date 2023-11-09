package com.weather.dto;

public record WeatherData(String main, String description, String icon, Double temp) {
}
