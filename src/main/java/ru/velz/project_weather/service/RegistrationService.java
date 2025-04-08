package ru.velz.project_weather.service;


import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.UserDao;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.exception.UserNotFoundException;
import ru.velz.project_weather.model.User;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public void registerUser(User user) {
        if (userDao.findUserByLogin(user.getLogin()).isPresent()) {
            throw new UserAlreadyExistsException("User with this login exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userDao.create(user);
    }

    public User findRegisteredUser(User user) {
        Optional<User> registeredUserOpt = userDao.findUserByLogin(user.getLogin());

        if (registeredUserOpt.isPresent()) {
            User registeredUser = registeredUserOpt.get();
            if (passwordEncoder.matches(user.getPassword(), registeredUser.getPassword())) {
                return registeredUser;
            }
        }
        throw new UserNotFoundException("Invalid login or password");
    }


}
