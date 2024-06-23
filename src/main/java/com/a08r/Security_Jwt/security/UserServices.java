package com.a08r.Security_Jwt.security;

import com.a08r.Security_Jwt.jwt.JwtServices;
import lombok.AllArgsConstructor;
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
public class UserServices
//        implements UserDetailsService
{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;
    private final UserDetailsService userDetailsService;

    public ResponseEntity<?> register(User user) throws Exception{
        User newUser = userRepository.save(user);
    //TODO: check if email is unique
        if (newUser.getEmail().isEmpty()) {
           // throw new Exception("Email cannot be empty");
            return ResponseEntity.badRequest().body("Email cannot be empty");
        }
        return new ResponseEntity<>(userMapper.getUserDTO(newUser), HttpStatus.CREATED);
    }

    public ResponseEntity<?> login(AuthRequest user) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken
                        (user.getUsername(), user.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            return new ResponseEntity<>(jwtServices.generateToken(userDetails), HttpStatus.OK);
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

        public ResponseEntity<?> refreshToken(String userEmail) {
        User user = userRepository.findByEmail(userEmail).get();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        return ResponseEntity.ok(jwtServices.generateToken(userDetails));
    }

}
