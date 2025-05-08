package ru.velz.project_weather.service;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.UserDao;
import ru.velz.project_weather.dto.LoginUserDto;
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.exception.UserNotFoundException;
import ru.velz.project_weather.mapper.UserMapper;
import ru.velz.project_weather.model.User;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public User registerUser(RegistrationUserDto registrationUserDto) {
        if (userDao.findUserByLogin(registrationUserDto.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException("User with this login exists");
        }

        User user = userMapper.mapToUser(registrationUserDto);
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDao.create(user);

        return user;
    }

    public User findRegisteredUser(LoginUserDto loginUserDto) {
        Optional<User> registeredUserOpt = userDao.findUserByLogin(loginUserDto.getLogin());

        if (registeredUserOpt.isPresent()) {
            User registeredUser = registeredUserOpt.get();

            if (passwordEncoder.matches(loginUserDto.getPassword(), registeredUser.getPassword())) {
                return registeredUser;
            }
        }
        throw new UserNotFoundException("Invalid login or password");
    }


}
