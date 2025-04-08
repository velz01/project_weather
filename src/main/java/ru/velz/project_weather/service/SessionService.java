package ru.velz.project_weather.service;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.SessionDao;
import ru.velz.project_weather.dao.UserDao;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.session.SessionNotFoundException;

import java.time.LocalDateTime;
import java.time.temporal.TemporalField;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SessionService {
    public static final int ONE_HOUR = 1;


    private final SessionDao sessionDao;

    public Session createSession(User user) {
        Session session = buildSession(user);
        sessionDao.create(session);

        return session;
    }

    private static Session buildSession(User user) {
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(ONE_HOUR);
        return Session.builder()
                .id(UUID.randomUUID())
                .user(user)
                .expiresAt(expiresAt)
                .build();

    }

    public Session findSessionById(UUID id) {

        Optional<Session> sessionOpt = sessionDao.findSessionById(id);

        if (sessionOpt.isPresent()) {
            Session session = sessionOpt.get();
            if (sessionIsExpired(session) ) {
                throw new SessionIsExpiredException("Session is expired");
            }
            return session;
        }
        throw new SessionNotFoundException("Session isn't found"); //ToDO: разработать scheduler для автоматической подчистки сессий
    }

    private static boolean sessionIsExpired(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now());
    }


}
