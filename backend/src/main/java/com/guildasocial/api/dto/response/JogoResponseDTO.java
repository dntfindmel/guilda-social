package com.guildasocial.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JogoResponseDTO {

    private UUID id;
    private String nome;
    private String tipo;
    private Integer minJogadores;
    private Integer maxJogadores;
    private Integer tempoMedioPartida;
    private String imagemUrl;
}
