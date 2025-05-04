import config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.config.SpringConfig;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.RegistrationService;
import ru.velz.project_weather.service.SessionService;

import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { SpringConfig.class, TestConfig.class })
@ActiveProfiles("test")
@Transactional
@WebAppConfiguration

public class SessionTest {


    public static final int ONE_HOUR = 1;
    private final SessionService sessionService;
    private final RegistrationService registrationService;

    @Autowired
    public SessionTest(SessionService sessionService, RegistrationService registrationService) {
        this.sessionService = sessionService;
        this.registrationService = registrationService;
    }

    private static User buildUser() {
        return User
                .builder()
                .login("legenda")
                .password("leon")
                .build();
    }

    @Test
    public void sessionExpiresAfterOneHour() {
        User user = buildUser();
        registrationService.registerUser(user);

        Session session = sessionService.createSession(user);
        Assertions.assertDoesNotThrow(() -> sessionService.findSessionById(session.getId()));

        session.setExpiresAt(LocalDateTime.now().minusHours(ONE_HOUR));
        sessionService.updateSession(session);

        Assertions.assertThrows(SessionIsExpiredException.class, () -> {
            sessionService.findSessionById(session.getId());
        });
    }
}
