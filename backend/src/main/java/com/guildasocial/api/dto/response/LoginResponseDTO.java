package com.guildasocial.api.dto.response;

import java.util.UUID;

public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private UUID usuarioId;
    private String nome;
    private String email;

    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, UUID usuarioId, String nome, String email) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.nome = nome;
        this.email = email;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(UUID usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}