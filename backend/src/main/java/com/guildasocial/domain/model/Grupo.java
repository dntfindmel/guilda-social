package com.guildasocial.domain.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "grupos")
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "jogo_id")
    private Jogo jogo;

    @ManyToOne
    @JoinColumn(name = "lider_id", nullable = false)
    private Usuario lider;

    private String local;

    @Column(name = "data_evento")
    private LocalDateTime dataEvento;

    @Column(nullable = false)
    private Integer vagas;

    @Column(name = "vagas_preenchidas")
    private Integer vagasPreenchidas = 0;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToMany
    @JoinTable(
        name = "grupo_membros",
        joinColumns = @JoinColumn(name = "grupo_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> membros = new ArrayList<>();

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public Usuario getLider() { return lider; }
    public void setLider(Usuario lider) { this.lider = lider; }

    public String getLocal() { return local; }
    public void setLocal(String local) { this.local = local; }

    public LocalDateTime getDataEvento() { return dataEvento; }
    public void setDataEvento(LocalDateTime dataEvento) { this.dataEvento = dataEvento; }

    public Integer getVagas() { return vagas; }
    public void setVagas(Integer vagas) { this.vagas = vagas; }

    public Integer getVagasPreenchidas() { return vagasPreenchidas; }
    public void setVagasPreenchidas(Integer vagasPreenchidas) { this.vagasPreenchidas = vagasPreenchidas; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }

    public List<Usuario> getMembros() { return membros; }
    public void setMembros(List<Usuario> membros) { this.membros = membros; }
}
