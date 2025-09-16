package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterDTO register(RegisterRequest request) {
        // Verificar se email já existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email já está em uso");
        }

        // Criar novo usuário
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Salvar no banco
        User savedUser = userRepository.save(user);

        // Retornar DTO (sem senha)
        return new RegisterDTO(
                savedUser.getId(),
                savedUser.getFullName(),
                savedUser.getEmail()
        );
    }
}
