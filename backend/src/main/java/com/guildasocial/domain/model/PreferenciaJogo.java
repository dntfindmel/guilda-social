package com.guildasocial.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "preferencias_jogo")
public class PreferenciaJogo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private PerfilJogador perfil;

    @ManyToOne
    @JoinColumn(name = "jogo_id", nullable = false)
    private Jogo jogo;

    @Column(name = "nivel_interesse", length = 10)
    private String nivelInteresse;

    @Column(name = "nivel_experiencia", length = 15)
    private String nivelExperiencia;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public PerfilJogador getPerfil() { return perfil; }
    public void setPerfil(PerfilJogador perfil) { this.perfil = perfil; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public String getNivelInteresse() { return nivelInteresse; }
    public void setNivelInteresse(String nivelInteresse) { this.nivelInteresse = nivelInteresse; }

    public String getNivelExperiencia() { return nivelExperiencia; }
    public void setNivelExperiencia(String nivelExperiencia) { this.nivelExperiencia = nivelExperiencia; }
}
