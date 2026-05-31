package com.authentication.authentication_system.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import com.authentication.authentication_system.dto.ErrorResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> validationErrors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      validationErrors.put(fieldName, error.getDefaultMessage());
    });
    return build(HttpStatus.BAD_REQUEST, "Validation Failed", "Input validation failed.", validationErrors);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateResourceException ex) {
    return build(HttpStatus.CONFLICT, "conflict", ex.getMessage(), null);
  }

  @ExceptionHandler(ResourceAccessException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    return build(HttpStatus.UNAUTHORIZED, "Unauthorizaed", ex.getMessage(), null);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    return build(HttpStatus.UNAUTHORIZED, "Unauthorizaed", "Invalid credentials", null);
  }

  @ExceptionHandler(ExpiredJwtException.class)
  public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex) {
    return build(HttpStatus.UNAUTHORIZED, "Token Expired", "Session expired. Please log in again.", null);
  }
  
  @ExceptionHandler({ MalformedJwtException.class, SignatureException.class })
  public ResponseEntity<ErrorResponse> handleInvalidJwt(Exception ex) {
    return build(HttpStatus.UNAUTHORIZED, "Invalid Token", "The provided token is invalid.", null);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    return build(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to access this resource.", null);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    ex.printStackTrace();
    return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred.", null);
  }

  private ResponseEntity<ErrorResponse> build(
    HttpStatus status, String error, String message, Map<String, String> validationErrors
  ) {
    ErrorResponse body = ErrorResponse.builder()
    .status(status.value())
    .error(error)
    .message(message)
        .timestamp(LocalDateTime.now())
        .validationErrors(validationErrors)
        .build();
    return ResponseEntity.status(status).body(body);
  }
}
