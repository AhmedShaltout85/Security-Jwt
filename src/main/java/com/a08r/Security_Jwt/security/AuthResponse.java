package com.a08r.Security_Jwt.security;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String refreshToken;
}
