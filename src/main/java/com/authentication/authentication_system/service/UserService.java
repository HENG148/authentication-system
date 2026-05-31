package com.authentication.authentication_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.authentication.authentication_system.dto.UserProfileResponse;
import com.authentication.authentication_system.exception.ResourceNotFoundException;
import com.authentication.authentication_system.model.User;
import com.authentication.authentication_system.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserProfileResponse getCurrentUserProfile() {
    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username;
    if (principal instanceof UserDetails) {
      username = ((UserDetails) principal).getUsername();
    } else {
      username = principal.toString();
    }
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    return toProfile(user);
  }

  public List<UserProfileResponse> getAllUser() {
    return userRepository.findAll().stream().map(this::toProfile).collect(Collectors.toList());
  }

  public UserProfileResponse getUserById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("ID cannot be null");
    }
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return toProfile(user);
  }

  private UserProfileResponse toProfile(User user) {
    return UserProfileResponse.builder()
    .id(user.getId()).username(user.getUsername())
    .email(user.getEmail()).username(user.getUsername())
    .createdAt(user.getCreatedAt())
        .build();
  }
}
