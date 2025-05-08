package ru.velz.project_weather.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.dto.WeatherDto;
import ru.velz.project_weather.model.Location;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.LocationService;
import ru.velz.project_weather.service.OpenWeatherApiService;
import ru.velz.project_weather.service.SessionService;

import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class LocationController {
    public static final String SESSION_COOKIE_NAME = "SESSION_ID";

    private final OpenWeatherApiService openWeatherApiService;
    private final LocationService locationService;
    private final SessionService sessionService;

    @GetMapping
    public String showLocations(HttpServletRequest request, Model model) {
        User user = getUser(request);
        model.addAttribute("user", user);

        List<Location> locationsByUserId = locationService.findLocationsByUserId(user.getId());
        List<WeatherDto> listWeatherDto = openWeatherApiService.obtainWeatherDtoByLocations(locationsByUserId);
        model.addAttribute("listWeatherDto", listWeatherDto);

        return "index";
    }


    @PostMapping("/locations")
    public String findLocations(HttpServletRequest request, @RequestParam(value = "name") String nameLocation, Model model) {
        List<LocationDto> locations = openWeatherApiService.obtainLocationsDtoByName(nameLocation);
        model.addAttribute("user", getUser(request));
        model.addAttribute(locations);

        return "search-results";
    }

    @PostMapping("/location")
    public String addLocation(HttpServletRequest request, @ModelAttribute LocationDto locationDto) {
        User user = getUser(request);
        locationService.addLocation(locationDto, user);

        return "redirect:/";

    }

    @DeleteMapping("/location")
    public String deleteLocation(@CookieValue(SESSION_COOKIE_NAME) Cookie cookie, @RequestParam(name = "locationId") Long locationId) {
        Session session = sessionService.findSessionById(UUID.fromString(cookie.getValue()));
        Long userId = session.getUser().getId();
        locationService.deleteLocation(locationId, userId);


        return "redirect:/";
    }


    private static User getUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }

}
