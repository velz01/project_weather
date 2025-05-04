package ru.velz.project_weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.exception.UserNotFoundException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.RegistrationService;
import ru.velz.project_weather.service.SessionService;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class AuthController {

    public static final String SESSION_COOKIE_NAME = "SESSION_ID";
    public static final int ONE_HOUR = 60 * 60;
    private final RegistrationService registrationService;

    private final SessionService sessionService;

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());

        return "sign-up";
    }


    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                               @RequestParam("repeatPassword") String repeatedPassword, Model model) {

        System.out.println(user);

        if (!user.getPassword().equals(repeatedPassword)) {
            bindingResult.addError(new FieldError("user", "password", "Password should be not different"));
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(user);
            return "sign-up";
        }

        try {
            registrationService.registerUser(user);
        } catch (UserAlreadyExistsException exception) {
            bindingResult.addError(new FieldError("user", "login", exception.getMessage()));
            return "sign-up";
        }

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new User());

        return "sign-in";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            model.addAttribute(user);
            return "sign-in";
        }

        try {
            User registeredUser = registrationService.findRegisteredUser(user);
            Session session = sessionService.createSession(registeredUser);
            setCookie(response, session);
            return "redirect:/";
        } catch (UserNotFoundException exception) {
            bindingResult.addError(new FieldError("user", "login", exception.getMessage()));
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
