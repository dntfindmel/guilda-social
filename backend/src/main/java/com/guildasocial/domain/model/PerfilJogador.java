package com.guildasocial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfis_jogador")
public class PerfilJogador {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estilo_jogo", nullable = false, length = 20)
    private EstiloJogo estiloJogo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "limite_distancia_km")
    private Integer limiteDistanciaKm = 50;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", length = 20)
    private NivelExperiencia nivelExperiencia;

    @OneToMany(mappedBy = "perfilJogador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PreferenciaJogo> preferenciasJogo = new ArrayList<>();

    @OneToMany(mappedBy = "perfilJogador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Disponibilidade> disponibilidades = new ArrayList<>();

    public enum EstiloJogo {
        CASUAL, COMPETITIVO, ROLEPLAYER, ESTRATEGISTA
    }

    public enum NivelExperiencia {
        INICIANTE, INTERMEDIARIO, AVANCADO
    }
}
