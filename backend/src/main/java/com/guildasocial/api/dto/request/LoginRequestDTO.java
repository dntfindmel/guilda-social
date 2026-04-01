package com.guildasocial.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequestDTO {
    
    private String email;
    private String senha;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "E-mail inválido")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank(message = "Senha é obrigatória")
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}