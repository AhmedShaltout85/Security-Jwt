package com.a08r.Security_Jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/free-access")
@RequiredArgsConstructor
public class FreeAccessController {

    @GetMapping()
    public ResponseEntity<String> all() {
        return ResponseEntity.ok().body("Any One Can Access All Data");
    }

}
