package com.a08r.Security_Jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/management")
@RequiredArgsConstructor
public class ManagementController {

    @GetMapping()
    public ResponseEntity<String> all() {
        return ResponseEntity.ok().body("Authorized Users Can Access Data");
    }

}
