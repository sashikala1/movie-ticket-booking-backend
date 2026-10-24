package com.example.movieticketbooking.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(this::formatError)
                .collect(Collectors.joining("; "));
        return buildResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse("Access denied", HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private String formatError(FieldError fieldError) {
        return fieldError.getField() + " " + fieldError.getDefaultMessage();
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
