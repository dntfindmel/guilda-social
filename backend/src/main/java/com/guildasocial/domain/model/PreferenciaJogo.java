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
@Table(name = "preferencias_jogo")
public class PreferenciaJogo {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilJogador perfilJogador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_interesse", nullable = false, length = 10)
    private NivelInteresse nivelInteresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_experiencia", nullable = false, length = 15)
    private NivelExperiencia nivelExperiencia;

    public enum NivelInteresse {
        BAIXO, MEDIO, ALTO
    }

    public enum NivelExperiencia {
        INICIANTE, INTERMEDIARIO, AVANCADO
    }
}
