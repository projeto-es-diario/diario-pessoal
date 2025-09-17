package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*") // Para desenvolvimento - remova em produção
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            // Validação de entrada
            if (registerRequest.getFullName() == null || registerRequest.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Nome completo é obrigatório"));
            }

            if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Email é obrigatório"));
            }

            if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Senha deve ter pelo menos 6 caracteres"));
            }

            // Verificar se email já existe
            Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail().trim().toLowerCase());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Este email já está em uso"));
            }

            // Criar novo usuário
            User user = new User();
            user.setFullName(registerRequest.getFullName().trim());
            user.setEmail(registerRequest.getEmail().trim().toLowerCase());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            User savedUser = userRepository.save(user);

            // Retornar DTO sem senha
            RegisterDTO response = new RegisterDTO(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createSuccessResponse("Usuário registrado com sucesso", response));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            // Validação de entrada
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Email é obrigatório"));
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(createErrorResponse("Senha é obrigatória"));
            }

            // Tentar autenticar
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail().trim().toLowerCase(),
                            loginRequest.getPassword()
                    )
            );

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String token = tokenService.generateToken(userDetails);

            // Buscar dados completos do usuário
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail().trim().toLowerCase());

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                AuthResponse authResponse = new AuthResponse(
                        token,
                        user.getId(),
                        user.getFullName(),
                        user.getEmail()
                );

                return ResponseEntity.ok(createSuccessResponse("Login realizado com sucesso", authResponse));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createErrorResponse("Erro ao buscar dados do usuário"));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Email ou senha inválidos"));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("Falha na autenticação: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    // Método auxiliar para criar respostas de erro padronizadas
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    // Método auxiliar para criar respostas de sucesso padronizadas
    private Map<String, Object> createSuccessResponse(String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", message);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}