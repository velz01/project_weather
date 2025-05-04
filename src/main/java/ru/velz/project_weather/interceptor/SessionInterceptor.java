package ru.velz.project_weather.interceptor;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.WebUtils;
import ru.velz.project_weather.exception.SessionIsExpiredException;
import ru.velz.project_weather.model.Session;
import ru.velz.project_weather.service.SessionService;
import ru.velz.project_weather.exception.SessionNotFoundException;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookieSessionId = WebUtils.getCookie(request, "SESSION_ID");

        if (cookieSessionId == null) {
            response.sendRedirect("/login");
            return false;
        }

        try {
            Session session = sessionService.findSessionById(UUID.fromString(cookieSessionId.getValue()));
            request.setAttribute("user", session.getUser());

            return true;
        } catch (SessionIsExpiredException | SessionNotFoundException exception) {
            response.sendRedirect("login");

            return false;
        }
    }
}
