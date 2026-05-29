package com.authentication.authentication_system.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {
  private int status;
  private String error;
  private String message;
  private LocalDateTime timestamp;
  private Map<String, String> validationErrors;

  public ErrorResponse() {
  }

  public ErrorResponse(int status, String error, String message, LocalDateTime timestamp,
      Map<String, String> validationErrors) {
    this.status = status;
    this.message = message;
    this.error = error;
    this.timestamp = timestamp;
    this.validationErrors = validationErrors;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> validationErrors;

    public Builder status(int status) {
      this.status = status;
      return this;
    }

    public Builder error(String error) {
      this.error = error;
      return this;
    }
    
    public Builder message(String message) {
      this.message = message;
      return this;
    }
        
    public Builder timestamp(LocalDateTime timestamp) {
      this.timestamp = timestamp;
      return this;
    }
        
    public Builder validationErrors(Map<String, String> v) {
      this.validationErrors = v;
      return this;
    }
        
    public ErrorResponse build() {
      return new ErrorResponse(status, error, message, timestamp, validationErrors);
    }
  }

  public int getStatus() {
    return status;
  }
  
  public void setStatus(int status) {
    this.status = status;
  }
    
  public String getError() {
    return error;
  }
    
  public void setError(String error) {
    this.error = error;
  }
    
  public String getMessage() {
    return message;
  }
    
  public void setMessage(String message) {
    this.message = message;
  }
    
  public LocalDateTime getTimestamp() {
    return timestamp;
  }
    
  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
    
  public Map<String, String> getValidationErrors() {
    return validationErrors;
  }

  public void setValidationErrors(Map<String, String> validationErrors) {
    this.validationErrors = validationErrors;
  } 
}
