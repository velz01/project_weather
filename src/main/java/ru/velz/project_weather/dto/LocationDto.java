package ru.velz.project_weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    private String name;
    private Coord coord;
    private Sys sys;


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Coord {
        @JsonProperty("lat")
        private BigDecimal latitude;
        @JsonProperty("lon")
        private BigDecimal longitude;
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

    public BigDecimal getLatitude() {
        return coord.getLatitude();
    }

    public BigDecimal getLongitude() {
        return coord.getLongitude();
    }

    public String getCountry() {
        return sys.getCountry();
    }
}