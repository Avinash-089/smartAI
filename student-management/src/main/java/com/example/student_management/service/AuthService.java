package com.example.student_management.service;

import com.example.student_management.dto.AuthDto;
import com.example.student_management.exception.BadRequestException;
import com.example.student_management.exception.DuplicateResourceException;
import com.example.student_management.model.AppUser;
import com.example.student_management.repository.AppUserRepository;
import com.example.student_management.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthDto.AuthResponse login(AuthDto.LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }

        Optional<AppUser> userOpt = userRepository.findByEmail(request.getEmail().trim());
        if (userOpt.isEmpty()) {
            throw new BadRequestException("Invalid email or password");
        }

        AppUser user = userOpt.get();
        boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword())
                || request.getPassword().equals(user.getPassword());

        if (!match) {
            throw new BadRequestException("Invalid email or password");
        }

        // Generate authentic, cryptographically signed JWT token
        String jwtToken = jwtUtils.generateToken(user.getEmail(), user.getRole(), user.getId());
        user.setToken(jwtToken);
        userRepository.save(user);

        return AuthDto.AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(jwtToken)
                .message("Login successful")
                .build();
    }

    public AuthDto.AuthResponse register(AuthDto.RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new BadRequestException("Password is required");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new BadRequestException("Name is required");
        }

        if (userRepository.existsByEmail(request.getEmail().trim())) {
            throw new DuplicateResourceException("Email already registered: " + request.getEmail());
        }

        String role = (request.getRole() != null && !request.getRole().isBlank())
                ? request.getRole().toUpperCase().trim() : "STUDENT";

        AppUser user = AppUser.builder()
                .name(request.getName().trim())
                .email(request.getEmail().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        AppUser saved = userRepository.save(user);

        // Generate JWT token upon registration
        String jwtToken = jwtUtils.generateToken(saved.getEmail(), saved.getRole(), saved.getId());
        saved.setToken(jwtToken);
        userRepository.save(saved);

        return AuthDto.AuthResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .role(saved.getRole())
                .token(jwtToken)
                .message("User registered successfully")
                .build();
    }

    public Optional<AppUser> getCurrentUser(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        if (token.regionMatches(true, 0, "Bearer ", 0, 7)) {
            token = token.substring(7).trim();
        }
        if (jwtUtils.validateToken(token)) {
            String email = jwtUtils.extractUsername(token);
            return userRepository.findByEmail(email);
        }
        return Optional.empty();
    }
}
