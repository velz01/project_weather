package ru.velz.project_weather.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.SessionDao;
import ru.velz.project_weather.dao.UserDao;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class LoginService {


    private final UserDao userDao;
    private final SessionDao sessionDao;
    private final BCryptPasswordEncoder passwordEncoder;




}
