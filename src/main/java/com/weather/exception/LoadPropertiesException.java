package com.weather.exception;

public class LoadPropertiesException extends RuntimeException {
    public LoadPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
