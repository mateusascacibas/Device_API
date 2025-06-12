package com.mateusascacibas.device_api.web.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.mateusascacibas.device_api.application.exception.DeviceNotFoundException;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleDeviceNotFoundException() {
        DeviceNotFoundException ex = new DeviceNotFoundException(99L);
        ResponseEntity<Object> response = handler.handleDeviceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Device Not Found", body.get("error"));
        assertTrue(body.get("message").toString().contains("99"));
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        ResponseEntity<Object> response = handler.handleIllegalArgument(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Bad Request", body.get("error"));
        assertEquals("Invalid input", body.get("message"));
    }

    @Test
    void shouldHandleIllegalStateException() {
        IllegalStateException ex = new IllegalStateException("Conflict detected");
        ResponseEntity<Object> response = handler.handleIllegalState(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Conflict", body.get("error"));
        assertEquals("Conflict detected", body.get("message"));
    }

    @Test
    void shouldHandleValidationErrors() {
        FieldError fieldError = new FieldError("deviceRequestDTO", "name", "cannot be blank");
        BindException bindException = new BindException(new Object(), "deviceRequestDTO");
        bindException.addError(fieldError);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindException);

        ResponseEntity<Map<String, Object>> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().get("error"));
    }

    @Test
    void shouldHandleInvalidJsonGeneric() {
        RuntimeException cause = new RuntimeException("Malformed JSON");
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Invalid JSON", cause);

        ResponseEntity<Map<String, Object>> response = handler.handleInvalidJson(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Malformed JSON", response.getBody().get("message"));
        assertEquals("Invalid request body", response.getBody().get("error"));
    }

}
