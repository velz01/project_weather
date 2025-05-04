package ru.velz.project_weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {
    @Getter
    @Setter
    private Long locationId;

    @Getter
    private String name;
    private List<Weather> weather;
    private Main main;
    private Sys sys;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Weather {
        private String main;
        private String description;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Main {
        private int temp;

        @JsonProperty("feels_like")
        private int feelsTemp;
        private int humidity;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Sys {
        private String country;
    }

    public String getMainWeather() {
        Weather primaryWeather = getPrimaryWeather();
        return primaryWeather.getMain();
    }
    public String getDescriptionWeather() {
        Weather primaryWeather = getPrimaryWeather();
        return primaryWeather.getDescription();
    }

    public Weather getPrimaryWeather() {
        return weather != null && !weather.isEmpty()
                ? weather.get(0)
                : null;
    }

    public int getTemperature() {
        return main.getTemp();
    }
    public int getFeelsTemp() {
        return main.getFeelsTemp();
    }

    public int getHumidity() {
        return main.getHumidity();
    }
    public String getCountry() {
        return sys.getCountry();
    }
}
