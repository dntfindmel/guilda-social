package com.guildasocial.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mensagens")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(name = "remetente_id", nullable = false)
    private UUID remetenteId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String conteudo;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean lida = false;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public UUID getRemetenteId() { return remetenteId; }
    public void setRemetenteId(UUID remetenteId) { this.remetenteId = remetenteId; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }

    public Boolean getLida() { return lida; }
    public void setLida(Boolean lida) { this.lida = lida; }
}
