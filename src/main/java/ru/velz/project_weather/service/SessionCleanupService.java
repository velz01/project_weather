package ru.velz.project_weather.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.velz.project_weather.dao.SessionDao;

@Service
@AllArgsConstructor
public class SessionCleanupService {
    public static final int ONE_MINUTE = 1000 * 60;

    private final SessionDao sessionDao;

    @Scheduled(fixedRate = ONE_MINUTE)
    public void removeExpiredSessions() {
        sessionDao.deleteExpiredSessions();
    }
}
