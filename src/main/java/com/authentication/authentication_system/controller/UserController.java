package com.authentication.authentication_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authentication.authentication_system.dto.UserProfileResponse;
import com.authentication.authentication_system.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/profile")
  public ResponseEntity<UserProfileResponse> getMyProfile() {
    return ResponseEntity.ok(userService.getCurrentUserProfile());
  }

  @GetMapping("/me")
  public ResponseEntity<String> whoAmI() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    return ResponseEntity.ok("Logged in as: " + username);
  }
}
