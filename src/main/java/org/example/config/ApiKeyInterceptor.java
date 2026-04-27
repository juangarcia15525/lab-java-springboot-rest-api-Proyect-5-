package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "API-Key";
    private static final String EXPECTED_API_KEY = "123456";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || apiKey.isBlank()) {
            throw new MissingApiKeyException("Falta el header API-Key");
        }

        if (!EXPECTED_API_KEY.equals(apiKey)) {
            throw new InvalidApiKeyException("API-Key invalida");
        }

        return true;
    }
}
