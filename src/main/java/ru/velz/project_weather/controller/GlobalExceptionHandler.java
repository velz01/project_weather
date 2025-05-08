package ru.velz.project_weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.exception.ApiRequestIsNotCorrectException;
import ru.velz.project_weather.exception.LocationNotFoundException;
import ru.velz.project_weather.exception.UserAlreadyExistsException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestIsNotCorrectException.class)
    public String handleApiRequestIsNotCorrectException(ApiRequestIsNotCorrectException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public String handleLocationNotFoundException(LocationNotFoundException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "error";
    }

}
