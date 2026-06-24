package com.wealth.service;

import com.wealth.dto.UserRegistrationDto;
import com.wealth.dto.UserResponseDto;
import com.wealth.entity.User;
import com.wealth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // This method handles the business logic and the mapping!
    public UserResponseDto registerUser(UserRegistrationDto userDto) {
        // 1. Map DTO to Entity
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword()); 
        user.setRole(userDto.getRole());

        // 2. Save Entity to Database
        User savedUser = userRepository.save(user);

        // 3. Map Entity back to Response DTO
        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }
}
