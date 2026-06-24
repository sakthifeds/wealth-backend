package com.wealth.dto;

import com.wealth.enums.Role;
import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private Role role;
}
