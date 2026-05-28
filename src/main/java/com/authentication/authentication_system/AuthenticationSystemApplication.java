package com.authentication.authentication_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthenticationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationSystemApplication.class, args);
		System.out.println("\n========================================");
    System.out.println("  Auth System started successfully!");
  	System.out.println("  API Base URL: http://localhost:8080");
    System.out.println("========================================\n");
	}
}
