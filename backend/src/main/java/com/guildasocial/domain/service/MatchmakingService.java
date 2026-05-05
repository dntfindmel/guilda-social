package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MatchmakingService {

    private final UsuarioRepository usuarioRepository;

    public MatchmakingService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> getSugestoes(UUID usuarioId) {
        return usuarioRepository.findAllAtivos(usuarioId);
    }

    public int calcularAfinidade(Usuario usuario1, Usuario usuario2) {
        int afinidade = 0;

        if (usuario1.getCidade() != null && usuario2.getCidade() != null &&
            usuario1.getCidade().equalsIgnoreCase(usuario2.getCidade())) {
            afinidade += 50;
        }

        if (usuario1.getEstado() != null && usuario2.getEstado() != null &&
            usuario1.getEstado().equalsIgnoreCase(usuario2.getEstado())) {
            afinidade += 30;
        }

        if (usuario1.getDescricao() != null && usuario2.getDescricao() != null) {
            afinidade += 20;
        }

        return Math.min(afinidade, 100);
    }
}
