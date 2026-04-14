package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.AtualizarPerfilRequestDTO;
import com.guildasocial.api.dto.response.PerfilResponseDTO;
import com.guildasocial.api.exception.ResourceNotFoundException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PerfilService {

    private final UsuarioRepository usuarioRepository;

    public PerfilService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public PerfilResponseDTO buscarPerfil(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        PerfilResponseDTO response = new PerfilResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setTelefone(usuario.getTelefone());
        response.setCidade(usuario.getCidade());
        response.setEstado(usuario.getEstado());
        response.setDescricao(usuario.getDescricao());
        response.setFotoPerfil(usuario.getFotoPerfil());
        response.setDataCadastro(usuario.getDataCadastro());
        response.setAtivo(usuario.getAtivo());

        return response;
    }

    @Transactional
    public PerfilResponseDTO atualizarPerfil(UUID usuarioId, AtualizarPerfilRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (request.getNome() != null && !request.getNome().isEmpty()) {
            usuario.setNome(request.getNome());
        }
        if (request.getTelefone() != null) {
            usuario.setTelefone(request.getTelefone());
        }
        if (request.getDescricao() != null) {
            usuario.setDescricao(request.getDescricao());
        }
        if (request.getCidade() != null && !request.getCidade().isEmpty()) {
            usuario.setCidade(request.getCidade());
        }
        if (request.getEstado() != null && !request.getEstado().isEmpty()) {
            usuario.setEstado(request.getEstado());
        }

        usuarioRepository.save(usuario);

        return buscarPerfil(usuarioId);
    }
}
