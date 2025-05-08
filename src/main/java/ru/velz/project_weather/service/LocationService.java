package ru.velz.project_weather.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.LocationDao;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.exception.LocationNotFoundException;
import ru.velz.project_weather.mapper.LocationMapper;
import ru.velz.project_weather.model.Location;
import ru.velz.project_weather.model.User;

import java.util.List;

@Service
@AllArgsConstructor
public class LocationService {

    private final LocationMapper locationMapper;
    private final LocationDao locationDao;

    public void addLocation(LocationDto locationDto, User user) {
        Location location = locationMapper.mapToLocation(locationDto, user);
        locationDao.create(location);
    }

    public List<Location> findLocationsByUserId(Long userId) {
        return locationDao.findLocationsByUserId(userId);
    }

    public void deleteLocation(Long locationId, Long userId) {
        Location location = locationDao.findLocationByUserIdAndByLocationId(locationId, userId)
                .orElseThrow(() -> new LocationNotFoundException("Location is not found or doesn't belong to you"));
        locationDao.delete(location);
    }



}
