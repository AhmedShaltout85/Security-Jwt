package com.a08r.Security_Jwt.security;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    public UserDTO getUserDTO(User user) {
        UserDTO newUser = new UserDTO();
        newUser.setFirstname(user.getFirstname());
        newUser.setLastname(user.getLastname());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(Role.valueOf(user.getRole().toString().toUpperCase()));
        return newUser;
    }
}
