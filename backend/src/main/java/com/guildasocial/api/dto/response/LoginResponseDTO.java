package com.guildasocial.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private UUID usuarioId;
    private String nome;
    private String email;
}
