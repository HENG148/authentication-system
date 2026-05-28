package com.authentication.authentication_system.model;

import jakarta.persi
import lombok.AllArgsConstructor;
import lombok.Builder;


import java.time.LocalDateTime;
import java.util.Collection;

@Table(name = "users",
    uniqueConstraints = {
        @uniqueConstraints(columnNames = "username"),
        @uniqueConstraints(columnNames = "email")
    }
)

public class User implements UserDetails{
  
}
