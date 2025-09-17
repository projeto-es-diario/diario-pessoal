package com.example.demo.security;

import com.example.demo.model.User;
import com.example.demo.model.UserRole;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @Test
    void generateAndValidateToken() throws Exception {
        
        TokenService tokenService = new TokenService();

        
        Field secretField = TokenService.class.getDeclaredField("secretKeyString");
        secretField.setAccessible(true);
        secretField.set(tokenService, "mySuperSecretKeyThatIsLongEnoughToUseHS256!!!");

        Field expField = TokenService.class.getDeclaredField("expiration");
        expField.setAccessible(true);
        expField.set(tokenService, 3600000L);

        
        tokenService.init();

        
        User user = new User();
        user.setEmail("user@test.com");
        user.setFullName("User Test");
        user.setRole(UserRole.USER);

        
        String token = tokenService.generateToken(user);
        assertNotNull(token, "Token não deve ser nulo");

        
        String username = tokenService.extractUsername(token);
        assertEquals("user@test.com", username);

        
        assertTrue(tokenService.validateToken(token, user), "Token deve ser válido para o usuário");
        assertTrue(tokenService.isTokenValid(token), "isTokenValid deve retornar true");
    }
}
