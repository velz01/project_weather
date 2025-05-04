package ru.velz.project_weather.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.velz.project_weather.dao.LocationDao;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.dto.WeatherDto;
import ru.velz.project_weather.exception.ApiRequestIsNotCorrectException;
import ru.velz.project_weather.mapper.LocationMapper;
import ru.velz.project_weather.model.Location;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.OpenWeatherApiService;

import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class LocationController {
    private final OpenWeatherApiService openWeatherApiService;
    private final LocationMapper locationMapper;
    private final LocationDao locationDao;

    @GetMapping
    public String showLocations(HttpServletRequest request, Model model) {
        try {
            User user = getUser(request);
            model.addAttribute("user", user);
            List<Location> locationsByUserId = locationDao.findLocationsByUserId(user.getId());
            List<WeatherDto> listWeatherDto = openWeatherApiService.obtainWeatherDtoByLocations(locationsByUserId);
            model.addAttribute("listWeatherDto", listWeatherDto);
        } catch (ApiRequestIsNotCorrectException exception) {
            return "error";
        }


        return "index";

    }


    @PostMapping("/locations")
    public String findLocations(HttpServletRequest request, @RequestParam(value = "name") String nameLocation, Model model){

        try {
            List<LocationDto> locations = openWeatherApiService.obtainLocationsDtoByName(nameLocation);
            model.addAttribute("user", getUser(request));
            model.addAttribute(locations);
        } catch (ApiRequestIsNotCorrectException exception) {
            return "error";
        }


        return "search-results";
    }

    @PostMapping("/location")
    public String addLocation(HttpServletRequest request, @ModelAttribute LocationDto locationDto) {
        User user = getUser(request);
        Location location = locationMapper.mapToLocation(locationDto, user);
        locationDao.create(location);

        return "redirect:/";

    }

    @DeleteMapping("/location")
    public String deleteLocation( @RequestParam(name = "locationId") Long locationId) {
        locationDao.delete(locationId);

        return "redirect:/";
    }


    private static User getUser(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }


}
