package com.guildasocial.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "usuario_id1", nullable = false)
    private Usuario usuario1;

    @ManyToOne
    @JoinColumn(name = "usuario_id2", nullable = false)
    private Usuario usuario2;

    @Column(name = "nivel_afinidade")
    private Integer nivelAfinidade;

    @Column(name = "data_match")
    private LocalDateTime dataMatch = LocalDateTime.now();

    @Column(length = 15)
    private String status = "PENDENTE";

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @JsonIgnore
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Mensagem> mensagens;

    @Transient
    private Mensagem ultimaMensagem;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Usuario getUsuario1() { return usuario1; }
    public void setUsuario1(Usuario usuario1) { this.usuario1 = usuario1; }

    public Usuario getUsuario2() { return usuario2; }
    public void setUsuario2(Usuario usuario2) { this.usuario2 = usuario2; }

    public Integer getNivelAfinidade() { return nivelAfinidade; }
    public void setNivelAfinidade(Integer nivelAfinidade) { this.nivelAfinidade = nivelAfinidade; }

    public LocalDateTime getDataMatch() { return dataMatch; }
    public void setDataMatch(LocalDateTime dataMatch) { this.dataMatch = dataMatch; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataResposta() { return dataResposta; }
    public void setDataResposta(LocalDateTime dataResposta) { this.dataResposta = dataResposta; }

    public List<Mensagem> getMensagens() { return mensagens; }
    public void setMensagens(List<Mensagem> mensagens) { this.mensagens = mensagens; }

    public Mensagem getUltimaMensagem() { return ultimaMensagem; }
    public void setUltimaMensagem(Mensagem ultimaMensagem) { this.ultimaMensagem = ultimaMensagem; }
}
