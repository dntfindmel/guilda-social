package com.guildasocial.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id1", nullable = false)
    private Usuario usuario1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id2", nullable = false)
    private Usuario usuario2;

    @Column(name = "nivel_afinidade", nullable = false)
    private Integer nivelAfinidade;

    @CreatedDate
    @Column(name = "data_match", nullable = false, updatable = false)
    private LocalDateTime dataMatch;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusMatch status = StatusMatch.PENDENTE;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    public enum StatusMatch {
        PENDENTE, ACEITO, RECUSADO, BLOQUEADO
    }
}
