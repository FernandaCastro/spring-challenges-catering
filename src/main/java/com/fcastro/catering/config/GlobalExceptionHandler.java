package com.fcastro.catering.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Clock;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<Object> statusCodeException(HttpClientErrorException ex, HttpServletRequest request) {

        CateringError cateringError = CateringError.builder()
                .timestamp(Clock.systemUTC().millis())
                .status(ex.getStatusCode().value())
                .errorType(ex.getClass().getSimpleName())
                .errorMessage("Resource was not found.")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatusCode()).body(cateringError);
    }
}
