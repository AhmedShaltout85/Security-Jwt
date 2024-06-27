package com.a08r.Security_Jwt.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data

public class AuthResponse {
    @JsonIgnoreProperties("access-token")
    private String accessToken;

    @JsonIgnoreProperties("refresh-token")
    private String refreshToken;
}
