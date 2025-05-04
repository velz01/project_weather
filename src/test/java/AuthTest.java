import config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.config.SpringConfig;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.RegistrationService;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestConfig.class, SpringConfig.class})
@ContextConfiguration(classes = { SpringConfig.class, TestConfig.class })
@ActiveProfiles("test")
@Transactional
@WebAppConfiguration
public class AuthTest {

    private final RegistrationService registrationService;

    private User userToRegister;

    @Autowired
    public AuthTest(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @BeforeEach
    public void setUp() {
        this.userToRegister = buildUser();
    }

    private static User buildUser() {
        return User
                .builder()
                .login("legenda")
                .password("leon")
                .build();
    }

    @Test
    public void registrationCreatesUser() {
        User userWithPlainPassword = buildUser();

        registrationService.registerUser(userToRegister);
        User registeredUser = registrationService.findRegisteredUser(userWithPlainPassword);

        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(registeredUser.getLogin(), userToRegister.getLogin());
    }

    @Test
    public void registrationWithDuplicateLoginThrowsException() {
        User duplicateUser = buildUser();

        registrationService.registerUser(userToRegister);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> registrationService.registerUser(duplicateUser));

    }


}
