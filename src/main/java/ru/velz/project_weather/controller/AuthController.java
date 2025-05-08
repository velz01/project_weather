package ru.velz.project_weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.velz.project_weather.dto.LoginUserDto;
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.exception.UserNotFoundException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.AuthenticationService;
import ru.velz.project_weather.service.SessionService;

import java.util.UUID;

@Slf4j
@Controller
@AllArgsConstructor
public class AuthController {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    public static final int ONE_HOUR = 60 * 60;
    private final AuthenticationService authenticationService;

    private final SessionService sessionService;

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new RegistrationUserDto());

        return "sign-up";
    }


    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid RegistrationUserDto registrationUserDto, BindingResult bindingResult,
                               Model model) {
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getRepeatedPassword())) {
            log.warn("Passwords doesn't match");
            bindingResult.rejectValue("password", "error.user","Password should be the same" );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(registrationUserDto);
            return "sign-up";
        }

        try {
            authenticationService.registerUser(registrationUserDto);
        } catch (UserAlreadyExistsException exception) {
            log.warn("User with login {} already exists", registrationUserDto.getLogin());
            bindingResult.rejectValue("login", "error.user",exception.getMessage());

            return "sign-up";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new LoginUserDto());

        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") @Valid LoginUserDto loginUserDto, BindingResult bindingResult, Model model, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(loginUserDto);
            return "sign-in";
        }

        try {
            User registeredUser = authenticationService.findRegisteredUser(loginUserDto);
            Session session = sessionService.createSession(registeredUser);
            setCookie(response, session);
            return "redirect:/";
        } catch (UserNotFoundException exception) {
            log.info("User with login {} doesn't exists or password isn't correct", loginUserDto.getLogin());
            bindingResult.rejectValue("login", "error.user", exception.getMessage() );

            return "sign-in";
        }

    }

    private static void setCookie(HttpServletResponse response, Session session) {
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId().toString());
        cookie.setMaxAge(ONE_HOUR);
        response.addCookie(cookie);
    }

    @DeleteMapping("/logout")
    public String logoutUser(@CookieValue(SESSION_COOKIE_NAME) Cookie cookie) {
        UUID sessionId = UUID.fromString(cookie.getValue());
        cookie.setValue(null);
        sessionService.deleteBySessionId(sessionId);

        return "redirect:/login";
    }
}
