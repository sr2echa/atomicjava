package com.sreecha.atomicjava.controller;

import com.sreecha.atomicjava.config.security.jwt.JwtUtils;
import com.sreecha.atomicjava.dto.LoginRequest;
import com.sreecha.atomicjava.dto.LoginResponse;
import com.sreecha.atomicjava.dto.RegisterRequest;
import com.sreecha.atomicjava.dto.UserResponse;
import com.sreecha.atomicjava.model.Role;
import com.sreecha.atomicjava.model.User;
import com.sreecha.atomicjava.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Check if username already exists
        if (userService.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        // Check if email already exists
        if (userService.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPasswordHash(registerRequest.getPassword());
        user.setRegistrationDate(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        if (registerRequest.getRoles() != null && !registerRequest.getRoles().isEmpty()) {
            roles = registerRequest.getRoles().stream()
                    .map(roleName -> {
                        try {
                            return Role.valueOf(roleName.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            return Role.USER;
                        }
                    })
                    .collect(Collectors.toSet());
        } else {
            roles.add(Role.USER);
        }
        user.setRoles(roles);

        UserResponse newUser = userService.createUser(user);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "User registered successfully");
        response.put("user", newUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUsernameOrEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            LoginResponse loginResponse = new LoginResponse(
                    jwt,
                    user.getId(),
                    userDetails.getUsername(),
                    user.getEmail(),
                    new HashSet<>(roles)
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Login successful");
            response.put("data", loginResponse);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid username/email or password");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}