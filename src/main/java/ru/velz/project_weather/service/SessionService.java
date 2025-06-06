package ru.velz.project_weather.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.SessionDao;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.exception.SessionNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
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
            if (sessionIsExpired(session)) {
                log.info("User's session is expired");
                throw new SessionIsExpiredException("Session is expired");
            }
            return session;
        }
        log.warn("Session is not found");
        throw new SessionNotFoundException("Session isn't found");
    }

    private static boolean sessionIsExpired(Session session) {
        return session.getExpiresAt().isBefore(LocalDateTime.now());
    }


    public void deleteBySessionId(UUID sessionId) {
        sessionDao.deleteBySessionId(sessionId);
    }

    public void updateSession(Session session) {
        sessionDao.update(session);
    }
}
