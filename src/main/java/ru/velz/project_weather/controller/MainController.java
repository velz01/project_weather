package ru.velz.project_weather.controller;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import ru.velz.project_weather.dao.UserDao;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.service.SessionService;
import ru.velz.project_weather.session.SessionNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class MainController {
    private final SessionService sessionService;

    @GetMapping
    public String getIndex(@CookieValue(value = "SESSION_ID", required = false) Cookie cookie, Model model) {

        try {
            if (cookie != null) {
                Session session = sessionService.findSessionById(UUID.fromString(cookie.getValue()));

                model.addAttribute("user", session.getUser());

            } else {
                return "redirect:/login";
            }
        } catch (SessionIsExpiredException | SessionNotFoundException exception) {
            return "redirect:/login";
        }


        return "index";

    }

}
