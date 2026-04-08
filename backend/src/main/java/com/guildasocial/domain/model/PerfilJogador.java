package com.guildasocial.domain.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "perfis_jogador")
public class PerfilJogador {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "estilo_jogo", length = 20)
    private String estiloJogo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "limite_distancia_km")
    private Integer limiteDistanciaKm = 50;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getEstiloJogo() { return estiloJogo; }
    public void setEstiloJogo(String estiloJogo) { this.estiloJogo = estiloJogo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Integer getLimiteDistanciaKm() { return limiteDistanciaKm; }
    public void setLimiteDistanciaKm(Integer limiteDistanciaKm) { this.limiteDistanciaKm = limiteDistanciaKm; }
}
