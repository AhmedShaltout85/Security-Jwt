package com.a08r.Security_Jwt.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) throws Exception {
        return new ResponseEntity<>(userServices.register(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest user) {
        return new ResponseEntity<>(userServices.login(user), HttpStatus.OK);
    }


    @GetMapping("/users")
    public ResponseEntity<?> loadAllUsers() {
        return new ResponseEntity<>(userServices.loadAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody String userEmail) {
        return new ResponseEntity<>(userServices.refreshToken(userEmail), HttpStatus.OK);
    }

}
