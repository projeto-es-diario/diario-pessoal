package com.example.demo.model;

public enum UserRole {
    USER("user"),
    ADMIN("admin"),
    MODERATOR("moderator");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}