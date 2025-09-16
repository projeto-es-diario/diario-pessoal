
// src/main/java/com/example/demo/dto/RegisterDTO.java
package com.example.demo.dto;

public class RegisterDTO {
    private Long id;
    private String fullName;
    private String email;
    private String role;

    // Construtores
    public RegisterDTO() {}

    public RegisterDTO(Long id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = "USER"; // Define um papel padrão
    }

    public RegisterDTO(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }
}