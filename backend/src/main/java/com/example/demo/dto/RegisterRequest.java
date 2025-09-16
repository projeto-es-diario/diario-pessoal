// src/main/java/com/example/demo/dto/RegisterRequest.java
package com.example.demo.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;

    // Construtores
    public RegisterRequest() {}

    public RegisterRequest(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Setters - Importantes para a desserialização do JSON
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}