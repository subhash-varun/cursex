package com.cursx.service.auth;

import com.cursx.dto.AuthResponse;
import com.cursx.dto.LoginRequest;
import com.cursx.dto.RegisterRequest;
import com.cursx.entity.User;
import com.cursx.repository.UserRepository;
import com.cursx.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken!");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .planType(User.PlanType.FREE)
                .build();

        user = userRepository.save(user);

        String token = jwtTokenProvider.generateTokenFromUser(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                               user.getRole().name(), user.getPlanType().name());
    }

    public AuthResponse login(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtTokenProvider.generateTokenFromUser(user);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                               user.getRole().name(), user.getPlanType().name());
    }
}
