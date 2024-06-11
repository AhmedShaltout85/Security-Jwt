package com.a08r.Security_Jwt.security;

import lombok.Data;

@Data
public class AuthRequest {

    private String username;
    private String password;
}
