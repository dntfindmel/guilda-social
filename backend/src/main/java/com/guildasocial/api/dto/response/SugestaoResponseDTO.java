package com.guildasocial.api.dto.response;

import java.util.UUID;

public class SugestaoResponseDTO {
    private UUID id;
    private String nome;
    private String cidade;
    private String estado;
    private String estiloJogo;
    private String descricao;
    private String fotoPerfil;
    private Integer nivelAfinidade;

    // Construtores
    public SugestaoResponseDTO() {}

    public SugestaoResponseDTO(UUID id, String nome, String cidade, String estado,
                                String estiloJogo, String descricao, String fotoPerfil,
                                Integer nivelAfinidade) {
        this.id = id;
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
        this.estiloJogo = estiloJogo;
        this.descricao = descricao;
        this.fotoPerfil = fotoPerfil;
        this.nivelAfinidade = nivelAfinidade;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getEstiloJogo() { return estiloJogo; }
    public void setEstiloJogo(String estiloJogo) { this.estiloJogo = estiloJogo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getFotoPerfil() { return fotoPerfil; }
    public void setFotoPerfil(String fotoPerfil) { this.fotoPerfil = fotoPerfil; }

    public Integer getNivelAfinidade() { return nivelAfinidade; }
    public void setNivelAfinidade(Integer nivelAfinidade) { this.nivelAfinidade = nivelAfinidade; }
    
}
