package com.loch.meetingplanner.common.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SampleException.class)
    public ResponseEntity<ErrorResponse> handleSampleException(SampleException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e,
            HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, e, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException e, HttpServletRequest request) {

        FieldError firstError = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .orElse(null);

        String message = (firstError != null)
                ? String.format("Invalid field '%s': %s", firstError.getField(), firstError.getDefaultMessage())
                : "Validation failed for request";

        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, Exception e,
            HttpServletRequest request) {
        ErrorResponse error = new ErrorResponse(
                status.value(),
                e.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(status).body(error);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String path) {
        ErrorResponse error = new ErrorResponse(status.value(), message, path);
        return ResponseEntity.status(status).body(error);
    }
}
