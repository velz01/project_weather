package ru.velz.project_weather.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.dto.WeatherDto;
import ru.velz.project_weather.exception.ApiRequestIsNotCorrectException;
import ru.velz.project_weather.exception.LocationNotFoundException;
import ru.velz.project_weather.model.Location;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OpenWeatherApiService {

    private Environment environment;
    private ObjectMapper objectMapper;
    private HttpClient httpClient;

    public List<LocationDto> obtainLocationsDtoByName(String nameCity) {
        try {
            String apiKey = environment.getRequiredProperty("openweather.api.key");
            String uri = environment.getRequiredProperty("openweather.api.location.url");
            nameCity = URLEncoder.encode(nameCity, StandardCharsets.UTF_8);

            HttpRequest request = buildHttpRequest(getUrlForSearchByName(uri, nameCity, apiKey));

            HttpResponse<String> response = getResponse(request);

            String listLocations = objectMapper.readTree(response.body()).get("list").toString();

            return objectMapper.readValue(listLocations,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, LocationDto.class));
        } catch (IOException | InterruptedException e) {
            log.error("Something wrong with api request");
            throw new ApiRequestIsNotCorrectException(e.getMessage());
        }

    }


    private static URI getUrlForSearchByName(String uri, String nameCity, String apiKey) {
        return UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("q", nameCity)
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .build()
                .toUri();

    }

    public List<WeatherDto> obtainWeatherDtoByLocations(List<Location> locations) {
        ArrayList<WeatherDto> listWeatherDto = new ArrayList<>();
        String apiKey = environment.getRequiredProperty("openweather.api.key");
        String uri = environment.getRequiredProperty("openweather.api.weather.url");

        for (Location location : locations) {
            HttpRequest request = buildHttpRequest(getUrlForSearchByLocations(uri, location, apiKey));
            try {
                HttpResponse<String> response = getResponse(request);
                WeatherDto weatherDto = objectMapper.readValue(response.body(), WeatherDto.class);
                weatherDto.setLocationId(location.getId());
                listWeatherDto.add(weatherDto);
            } catch (IOException | InterruptedException e) {
                log.error("Something wrong with api request");
                throw new ApiRequestIsNotCorrectException(e.getMessage());
            }
        }

        return listWeatherDto;
    }

    private HttpResponse<String> getResponse(HttpRequest request) throws IOException, InterruptedException {

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            log.error("Location is not found");
            throw new LocationNotFoundException("Location not found.");
        }

        if (response.statusCode() != 200) {
            throw new ApiRequestIsNotCorrectException("Exception on Weather API");
        }

        return response;
    }

    private static HttpRequest buildHttpRequest(URI uri) {
        return HttpRequest
                .newBuilder()
                .uri(uri)
                .GET()
                .build();
    }

    private static URI getUrlForSearchByLocations(String uri, Location location, String apiKey) {
        return UriComponentsBuilder
                .fromUriString(uri)
                .queryParam("lat", location.getLatitude())
                .queryParam("lon", location.getLongitude())
                .queryParam("units", "metric")
                .queryParam("appid", apiKey)
                .build()
                .toUri();

    }


}
