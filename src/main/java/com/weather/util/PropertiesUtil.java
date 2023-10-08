package com.weather.util;

import com.weather.exception.LoadPropertiesException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    private PropertiesUtil() {
        throw new IllegalArgumentException("Util class");
    }

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (var resources = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resources);
        } catch (IOException e) {
            throw new LoadPropertiesException("Cannot load properties", e);
        }
    }
}
