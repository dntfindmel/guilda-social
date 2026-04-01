package com.guildasocial.api.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {

    private UUID id;
    private String nome;
    private String email;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private String dataNascimento;

    private String telefone;
    private String cidade;
    private String estado;
    private Double latitude;
    private Double longitude;
    private String descricao;
    private String fotoPerfil;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataCadastro;

    private Boolean ativo;
}
