package com.guildasocial.api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class MatchResponseDTO {
    private UUID id;
    private UUID usuarioId;
    private String usuarioNome;
    private Integer nivelAfinidade;
    private String status;
    private LocalDateTime dataMatch;

    // Construtores
    public MatchResponseDTO() {}

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getUsuarioId() { return usuarioId; }
    public void setUsuarioId(UUID usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public Integer getNivelAfinidade() { return nivelAfinidade; }
    public void setNivelAfinidade(Integer nivelAfinidade) { this.nivelAfinidade = nivelAfinidade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataMatch() { return dataMatch; }
    public void setDataMatch(LocalDateTime dataMatch) { this.dataMatch = dataMatch; }
}
