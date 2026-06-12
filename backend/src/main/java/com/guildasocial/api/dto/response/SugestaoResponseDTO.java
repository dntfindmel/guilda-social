package com.guildasocial.api.dto.response;

import java.util.UUID;

public class SugestaoResponseDTO {
    private UUID id;
    private String nome;
    private String cidade;
    private String estado;
    private String descricao;
    private String fotoPerfil;
    private Integer nivelAfinidade;
    private Integer nivel;
    private Integer distancia;
    private Integer idade;
    private String[] tags;

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public Integer getNivelAfinidade() { return nivelAfinidade; }
    public void setNivelAfinidade(Integer nivelAfinidade) { this.nivelAfinidade = nivelAfinidade; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public Integer getDistancia() { return distancia; }
    public void setDistancia(Integer distancia) { this.distancia = distancia; }

    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }

    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }
}
