package com.example.demo;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.controller.AuthController;
import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.TokenService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TokenService tokenService;

    @Test
    void shouldAuthenticateSuccessfully() throws Exception {
        String json = """
                {"email":"user@test.com","password":"123456"}
                """;

        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setFullName("User Test");
        user.setRole(UserRole.USER);

        
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(tokenService.generateToken(any(UserDetails.class))).thenReturn("token-123");


        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("token-123"));
    }

    @Test
    void shouldRegisterSuccessfully() throws Exception {
        String json = """
                {"fullName":"New User","email":"new@test.com","password":"abcdef"}
                """;

        User saved = new User();
        saved.setId(77L);
        saved.setEmail("new@test.com");
        saved.setFullName("New User");
        saved.setRole(UserRole.USER);

        when(userRepository.findByEmail("new@test.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(77L);
            return u;
        });

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("new@test.com"));
    }
}
