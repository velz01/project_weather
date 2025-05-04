import com.fasterxml.jackson.databind.ObjectMapper;
import config.TestConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.dto.LocationDto;
import ru.velz.project_weather.exception.ApiRequestIsNotCorrectException;
import ru.velz.project_weather.exception.LocationNotFoundException;
import ru.velz.project_weather.service.OpenWeatherApiService;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
@TestPropertySource("classpath:test_api.properties")
@Transactional
@WebAppConfiguration

public class ApiTest {


    @Autowired
    private Environment environment;
    private String locationJson;
    @Autowired
    private HttpClient httpClient;

    private HttpResponse<String> response;
    @Autowired
    private OpenWeatherApiService openWeatherApiService;

    @BeforeEach
    void setUp() throws IOException {
        locationJson = Files.readString(Path.of("src/test/resources/location_info.json"));
        httpClient = mock(HttpClient.class);
        response = mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(200);
        openWeatherApiService = new OpenWeatherApiService(environment, new ObjectMapper(), httpClient);
    }

    @Test
    public void obtainLocationsParsesResponseCorrectly() throws IOException, InterruptedException {
        String city = "London";

        when(response.body()).thenReturn(locationJson);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))).thenReturn(response);

        List<LocationDto> locationDtos = openWeatherApiService.obtainLocationsDtoByName(city);
        LocationDto locationDto = locationDtos.getFirst();
        Assertions.assertEquals(city, locationDto.getName());

    }
    @Test
    public void throwExceptionIfStatusCodeIs404() throws IOException, InterruptedException {

        when(response.statusCode()).thenReturn(404);

        when(
                httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString()))
        ).thenReturn(response);

        org.junit.jupiter.api.Assertions.assertThrows(LocationNotFoundException.class, () -> openWeatherApiService.obtainLocationsDtoByName("London"));
    }
}
