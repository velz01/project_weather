package ru.velz.project_weather.mapper;

import org.springframework.stereotype.Component;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.model.Location;
import ru.velz.project_weather.model.User;

@Component
public class LocationMapper {
    public Location mapToLocation(LocationDto locationDto, User user) {
        return Location
                .builder()
                .name(locationDto.getName())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .user(user)
                .build();
    }
}
