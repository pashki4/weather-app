package com.weather.exception;

public class LocationDaoException extends RuntimeException {
    public LocationDaoException(String message, Throwable reason) {
        super(message, reason);
    }
}
