package com.a08r.Security_Jwt.security;

import com.a08r.Security_Jwt.jwt.JwtServices;
import com.a08r.Security_Jwt.token.Token;
import com.a08r.Security_Jwt.token.TokenRepository;
import com.a08r.Security_Jwt.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServices {
    private final IUserRepository iUserRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;


    public ResponseEntity<?> register(User user) {
        if (iUserRepository.findByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email Already Exists", HttpStatus.BAD_REQUEST);
        }
        User newUser = iUserRepository.save(user);
        UserDTO userDTO = userMapper.getUserDTO(newUser);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        String token = jwtServices.generateToken(userDetails);
        String refreshToken = jwtServices.generateRefreshToken(userDetails);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(token);
        authResponse.setRefreshToken(refreshToken);
        //
        saveUserToken(newUser, token);
        //
        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }


    public ResponseEntity<AuthResponse> login(AuthRequest user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(jwtServices.generateToken(userDetails));
            authResponse.setRefreshToken(jwtServices.generateRefreshToken(userDetails));
            //
            revokeAllUserTokens(iUserRepository.findByEmail(user.getUsername()).get());
            saveUserToken(iUserRepository.findByEmail(user.getUsername()).get(), jwtServices.generateRefreshToken(userDetails));
            //
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


    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtServices.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.iUserRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtServices.isTokenValid(refreshToken, user)) {
                var accessToken = jwtServices.generateToken(user);
                revokeAllUserTokens(user); // revoke all previous tokens PRIVATE METHOD
                saveUserToken(user, accessToken); // save new token PRIVATE METHOD
                var authResponse = new AuthResponse();
                authResponse.setAccessToken(accessToken);
                authResponse.setRefreshToken(refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    // PRIVATE METHODS Refactored for reusability in UserServices
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}

