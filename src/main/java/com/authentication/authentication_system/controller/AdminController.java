package com.authentication.authentication_system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.authentication_system.dto.UserProfileResponse;
import com.authentication.authentication_system.service.UserService;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }
  @GetMapping("/dashboard")
  public ResponseEntity<Map<String, Object>> getDashboard() {
    Map<String, Object> dashboard = new HashMap<>();
    dashboard.put("message", "Welcome to the Admin Dashboard");
    dashboard.put("toalUsers", userService.getAllUser().size());
    dashboard.put("status", "System operational");
    return ResponseEntity.ok(dashboard);
  }

  @GetMapping("/users")
  public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
    return ResponseEntity.ok(userService.getAllUser());
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<UserProfileResponse> getUserById(@PathVariable Long id) {
    return ResponseEntity.ok(userService.getUserById(id));
  }

  @GetMapping("/check")
  public ResponseEntity<?> check() {
    return ResponseEntity.ok(userService.getAllUser());
  }
}
