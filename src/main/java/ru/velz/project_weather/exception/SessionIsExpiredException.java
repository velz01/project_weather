package ru.velz.project_weather.exception;

public class SessionIsExpiredException extends RuntimeException {
    public SessionIsExpiredException(String message) {
        super(message);
    }
}
