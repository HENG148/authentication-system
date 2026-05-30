package com.authentication.authentication_system.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.authentication.authentication_system.dto.AuthResponse;
import com.authentication.authentication_system.dto.LoginRequest;
import com.authentication.authentication_system.dto.RegisterRequest;
import com.authentication.authentication_system.exception.DuplicateResourceException;
import com.authentication.authentication_system.exception.InvalidCredentialsException;
import com.authentication.authentication_system.model.Role;
import com.authentication.authentication_system.model.User;
import com.authentication.authentication_system.repository.UserRepository;
import com.authentication.authentication_system.security.JwtService;

@Service
public class AuthService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
      AuthenticationManager authenticationManager) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.authenticationManager = authenticationManager;
  }

  public AuthResponse register(RegisterRequest request) {
    if (userRepository.existsByUsername(request.getUsername())) {
      throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken.");
    }
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateResourceException("Eamil '" + request.getEmail() + "' is already registered");
    }
    User user = User.builder()
        .username(request.getUsername())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

    User saved = userRepository.save(user);
    String token = jwtService.generateToken(saved);
    return AuthResponse.builder()
    .token(token).type("Bearer")
    .id(saved.getId()).username(saved.getUsername())
    .email(saved.getEmail()).role(saved.getRole().name())
    .message("Registration successful! Welcome, " + saved.getUsername() + "!")
        .build();
  }

  public AuthResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new InvalidCredentialsException("Invalid credentials. Please check your username and password.");
    }
    User user = userRepository.findByUsername(request.getUsername())
        .orElseThrow(() -> new InvalidCredentialsException("User not found."));

    String token = jwtService.generateToken(user);
    return AuthResponse.builder()
    .token(token).type("Bearer")
    .id(user.getId()).username(user.getUsername())
    .email(user.getEmail()).role(user.getRole().name())
    .message("Login successful! Welcome back, " + user.getUsername() + "!")
        .build();
  }
}
