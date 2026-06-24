package com.wealth.controller;

import com.wealth.dto.UserRegistrationDto;
import com.wealth.dto.UserResponseDto;
import com.wealth.dto.UserResponseDto;
import com.wealth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // 1. First API: Create a new user (Registration)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userDto) {
        // The Controller only handles HTTP routing and status codes
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        // Delegate the actual mapping and saving to the Service layer
        UserResponseDto responseDto = userService.registerUser(userDto);

        return ResponseEntity.ok(responseDto);
    }

    // 2. Second API: Get all users
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }
}
