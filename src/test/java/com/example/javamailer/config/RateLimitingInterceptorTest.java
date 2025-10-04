package com.example.javamailer.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitingInterceptorTest {

    private RateLimitingInterceptor interceptor;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Object handler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        interceptor = new RateLimitingInterceptor();
    }

    @Test
    void testRateLimitExceeded() throws Exception {
        // Mock request
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);

        // Mock response
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);

        // Make 11 requests (exceeding the limit of 10)
        boolean result = true;
        for (int i = 0; i < 11; i++) {
            result = interceptor.preHandle(request, response, handler);
        }

        // Verify rate limit was exceeded
        assertFalse(result);
        verify(response).setStatus(429);
        verify(response).setContentType("application/json");
    }

    @Test
    void testRateLimitNotExceeded() throws Exception {
        // Mock request
        when(request.getRemoteAddr()).thenReturn("192.168.1.2");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);

        // Make 5 requests (within the limit of 10)
        boolean result = true;
        for (int i = 0; i < 5; i++) {
            result = interceptor.preHandle(request, response, handler);
        }

        // Verify rate limit was not exceeded
        assertTrue(result);
        verify(response, never()).setStatus(429);
    }
}