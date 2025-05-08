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
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.AuthenticationService;
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
    private final AuthenticationService authenticationService;

    @Autowired
    public SessionTest(SessionService sessionService, AuthenticationService authenticationService) {
        this.sessionService = sessionService;
        this.authenticationService = authenticationService;
    }

    private static RegistrationUserDto buildRegistrationUserDto() {
        return RegistrationUserDto
                .builder()
                .login("legenda")
                .password("leon")
                .build();
    }

    @Test
    public void sessionExpiresAfterOneHour() {
        RegistrationUserDto registrationUserDto = buildRegistrationUserDto();
        User user = authenticationService.registerUser(registrationUserDto);

        Session session = sessionService.createSession(user);
        Assertions.assertDoesNotThrow(() -> sessionService.findSessionById(session.getId()));

        session.setExpiresAt(LocalDateTime.now().minusHours(ONE_HOUR));
        sessionService.updateSession(session);

        Assertions.assertThrows(SessionIsExpiredException.class, () -> {
            sessionService.findSessionById(session.getId());
        });
    }
}
