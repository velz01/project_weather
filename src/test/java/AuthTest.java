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
import ru.velz.project_weather.dto.LoginUserDto;
import ru.velz.project_weather.dto.RegistrationUserDto;
import ru.velz.project_weather.exception.UserAlreadyExistsException;
import ru.velz.project_weather.model.User;
import ru.velz.project_weather.service.AuthenticationService;

@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {TestConfig.class, SpringConfig.class})
@ContextConfiguration(classes = { SpringConfig.class, TestConfig.class })
@ActiveProfiles("test")
@Transactional
@WebAppConfiguration
public class AuthTest {

    private final AuthenticationService authenticationService;

    private RegistrationUserDto registrationUserDto;

    @Autowired
    public AuthTest(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @BeforeEach
    public void setUp() {
        this.registrationUserDto = buildUser();
    }

    private static RegistrationUserDto buildUser() {
        return RegistrationUserDto
                .builder()
                .login("legenda")
                .password("leon")
                .build();
    }
    private static LoginUserDto buildLoginDto(RegistrationUserDto regDto) {
        return LoginUserDto.builder()
                .login(regDto.getLogin())
                .password(regDto.getPassword())
                .build();
    }
    @Test
    public void registrationCreatesUser() {
        RegistrationUserDto userWithPlainPassword = buildUser();

        authenticationService.registerUser(registrationUserDto);
        User registeredUser = authenticationService.findRegisteredUser(buildLoginDto(userWithPlainPassword));

        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals(registeredUser.getLogin(), registrationUserDto.getLogin());
    }

    @Test
    public void registrationWithDuplicateLoginThrowsException() {
        RegistrationUserDto duplicateUser = buildUser();

        authenticationService.registerUser(registrationUserDto);

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> authenticationService.registerUser(duplicateUser));

    }


}
