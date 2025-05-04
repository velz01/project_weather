package ru.velz.project_weather.exception;

public class ApiRequestIsNotCorrectException extends RuntimeException {
    public ApiRequestIsNotCorrectException(String message) {
        super(message);
    }
}
