package com.linguafy.services;

import org.springframework.security.authentication.BadCredentialsException;
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

    public AuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponseDTO register(AuthRegisterRequestDTO dto) {
        userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            throw new IllegalArgumentException("E-mail já cadastrado");
        });

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setLevel(dto.getLevel());

        User saved = userRepository.save(user);
        return toAuthResponse(saved);
    }

    public AuthResponseDTO login(AuthLoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciais inválidas"));

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BadCredentialsException("Credenciais inválidas");
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