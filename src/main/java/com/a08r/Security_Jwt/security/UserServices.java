package com.a08r.Security_Jwt.security;

import com.a08r.Security_Jwt.jwt.JwtServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final IUserRepository iUserRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;
    private final UserDetailsService userDetailsService;


    public ResponseEntity<AuthResponse> register(User user) throws Exception {
        User newUser = iUserRepository.save(user);
        UserDTO userDTO = userMapper.getUserDTO(newUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        String token = jwtServices.generateToken(userDetails);
        String refreshToken = jwtServices.generateRefreshToken(userDetails);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setRefreshToken(refreshToken);
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    public ResponseEntity<AuthResponse> login(AuthRequest user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtServices.generateToken(userDetails));
            authResponse.setRefreshToken(jwtServices.generateRefreshToken(userDetails));
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        }

        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<User> loadUserById(Integer id) {
        return ResponseEntity.ok(iUserRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id.toString())));
    }

    public ResponseEntity<List<User>> loadAllUsers() {
        return new ResponseEntity<>(iUserRepository.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<?> updateUser(User user) {
        User newUser = iUserRepository.save(user);
        return ResponseEntity.ok(userMapper.getUserDTO(newUser));
    }


    public ResponseEntity<?> deleteUser(Integer id) {
        iUserRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }



}
