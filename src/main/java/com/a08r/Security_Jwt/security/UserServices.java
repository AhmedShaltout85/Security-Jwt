package com.a08r.Security_Jwt.security;

import com.a08r.Security_Jwt.jwt.JwtServices;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServices implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public ResponseEntity<?> register(User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.getUserDTO(newUser));
    }

    public ResponseEntity<?> login(AuthRequest user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = loadUserByUsername(user.getUsername());
            return ResponseEntity.ok(jwtServices.generateToken(userDetails));
        }
        return ResponseEntity.badRequest().build();
    }
    public ResponseEntity<User> loadUserById(Integer id) {
        return ResponseEntity.ok(userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id.toString())));
    }

    public ResponseEntity<List<User>> loadAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> updateUser(User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.ok(userMapper.getUserDTO(newUser));
    }


    public ResponseEntity<?> deleteUser(Integer id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //    public ResponseEntity<?> refreshToken(User user) {
//        UserDetails userDetails = loadUserByUsername(user.getEmail());
//        return ResponseEntity.ok(jwtServices.generateToken(userDetails));
//    }
//
}
