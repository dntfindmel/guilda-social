package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    private final UsuarioRepository usuarioRepository;

    public MatchmakingService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Busca sugestões de jogadores para o usuário atual
     * Exclui o próprio usuário e retorna todos os outros usuários ativos
     */
    public List<Usuario> getSugestoes(UUID usuarioId) {
        // Buscar todos os usuários ativos, excluindo o usuário logado
        List<Usuario> todosUsuarios = usuarioRepository.findAllAtivos();

        // Filtrar para excluir o próprio usuário
        return todosUsuarios.stream()
                .filter(usuario -> !usuario.getId().equals(usuarioId))
                .collect(Collectors.toList());
    }

    /**
     * Calcula afinidade entre dois usuários (simplificado para demonstração)
     */
    public int calcularAfinidade(Usuario usuario1, Usuario usuario2) {
        int afinidade = 0;

        // Mesma cidade = +50 pontos
        if (usuario1.getCidade() != null && usuario2.getCidade() != null &&
            usuario1.getCidade().equalsIgnoreCase(usuario2.getCidade())) {
            afinidade += 50;
        }

        // Mesmo estado = +30 pontos
        if (usuario1.getEstado() != null && usuario2.getEstado() != null &&
            usuario1.getEstado().equalsIgnoreCase(usuario2.getEstado())) {
            afinidade += 30;
        }

        // Mesmo estilo de jogo = +20 pontos (se tiver perfil)
        // Aqui você pode adicionar mais lógica conforme o perfil do jogador

        // Garantir que não ultrapasse 100
        return Math.min(afinidade, 100);
    }
}
