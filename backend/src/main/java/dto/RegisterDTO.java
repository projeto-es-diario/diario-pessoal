package br.com.diario.dto;

public class RegisterDTO {
    private String nomeCompleto;
    private String email;
    private String senha;

    // Getters
    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}