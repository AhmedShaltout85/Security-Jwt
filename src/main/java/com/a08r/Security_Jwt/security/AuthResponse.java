package com.a08r.Security_Jwt.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AuthResponse {
    @JsonProperty("access-token")
    private String accessToken;

    @JsonProperty("refresh-token")
    private String refreshToken;
}
