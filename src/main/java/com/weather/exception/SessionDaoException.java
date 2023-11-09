package com.weather.exception;

public class SessionDaoException extends RuntimeException {
    public SessionDaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
