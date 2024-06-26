package com.a08r.Security_Jwt.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServices userServices;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        return new ResponseEntity<>(userServices.register(user).getBody(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest user) {
        return new ResponseEntity<>(userServices.login(user).getBody(), HttpStatus.OK);
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> loadAllUsers() {
        return new ResponseEntity<>((userServices.loadAllUsers().getBody()), HttpStatus.OK);
    }



}
