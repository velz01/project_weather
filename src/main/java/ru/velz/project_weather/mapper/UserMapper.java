package ru.velz.project_weather.mapper;

import org.springframework.stereotype.Component;
import ru.velz.project_weather.dto.LoginUserDto;
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.model.User;

@Component
public class UserMapper {
    public User mapToUser(RegistrationUserDto registrationUserDto) {
        return User.builder()
                .login(registrationUserDto.getLogin())
                .password(registrationUserDto.getPassword())
                .build();
    }
}
