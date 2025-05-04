package ru.velz.project_weather.config;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.velz.project_weather.interceptor.SessionInterceptor;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
                .addInterceptor(sessionInterceptor)
                .excludePathPatterns(
                        "/login", "/css/**", "/js/**", "/images/**", "/registration"
                );
    }
}
