package com.guildasocial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jogos")
public class Jogo {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoJogo tipo;

    @Column(name = "min_jogadores", nullable = false)
    private Integer minJogadores;

    @Column(name = "max_jogadores", nullable = false)
    private Integer maxJogadores;

    @Column(name = "tempo_medio_partida")
    private Integer tempoMedioPartida;

    @Column(name = "imagem_url")
    private String imagemUrl;

    public enum TipoJogo {
        TABULEIRO, CARD_GAME, RPG_MESA, WAR_GAME
    }
}
