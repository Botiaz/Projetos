package com.linguafy.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.linguafy.dto.AuthLoginRequestDTO;
import com.linguafy.dto.AuthRegisterRequestDTO;
import com.linguafy.dto.AuthResponseDTO;
import com.linguafy.entities.User;
import com.linguafy.repositories.UserRepository;
import com.linguafy.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponseDTO register(AuthRegisterRequestDTO dto) {
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase();
        String password = dto.getPassword() == null ? "" : dto.getPassword().trim();
        String name = dto.getName() == null ? "" : dto.getName().trim();
        String level = dto.getLevel() == null ? "" : dto.getLevel().trim();

        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            throw new IllegalArgumentException("Nome, e-mail e senha são obrigatórios");
        }

        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("E-mail já cadastrado");
        });

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setLevel(level);

        User saved = userRepository.save(user);
        return toAuthResponse(saved);
    }

    public AuthResponseDTO login(AuthLoginRequestDTO dto) {
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase();
        String password = dto.getPassword() == null ? "" : dto.getPassword();

        if (email.isBlank() || password.isBlank()) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());
        boolean legacyPlainTextMatches = user.getPassword().equals(password);

        if (!passwordMatches && !legacyPlainTextMatches) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        if (legacyPlainTextMatches) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        }

        return toAuthResponse(user);
    }

    private AuthResponseDTO toAuthResponse(User user) {
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(jwtService.generateToken(user.getEmail()));
        response.setUserId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        return response;
    }
}