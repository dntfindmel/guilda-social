package com.guildasocial.domain.service;

import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.MatchRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final UsuarioRepository usuarioRepository;
    private final MatchmakingService matchmakingService;

    public MatchService(MatchRepository matchRepository,
                        UsuarioRepository usuarioRepository,
                        MatchmakingService matchmakingService) {
        this.matchRepository = matchRepository;
        this.usuarioRepository = usuarioRepository;
        this.matchmakingService = matchmakingService;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getSugestoes(UUID usuarioId) {
        Usuario usuarioAtual = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        List<Usuario> todosUsuarios = usuarioRepository.findAllAtivos(usuarioId);
        List<Usuario> sugestoes = new ArrayList<>();

        for (Usuario usuario : todosUsuarios) {
            // Não sugerir a si mesmo
            if (usuario.getId().equals(usuarioId)) continue;

            // Não sugerir usuários já conectados
            if (matchRepository.existsByUsuario1AndUsuario2(usuarioAtual, usuario)) continue;

            int afinidade = matchmakingService.calcularAfinidade(usuarioAtual, usuario);
            if (afinidade >= 50) { // Só sugerir se afinidade >= 50%
                sugestoes.add(usuario);
            }
        }

        // Ordenar por afinidade (maior primeiro)
        sugestoes.sort((u1, u2) -> {
            int afinidade1 = matchmakingService.calcularAfinidade(usuarioAtual, u1);
            int afinidade2 = matchmakingService.calcularAfinidade(usuarioAtual, u2);
            return Integer.compare(afinidade2, afinidade1);
        });

        return sugestoes;
    }

    @Transactional
    public Match enviarSolicitacao(UUID usuarioId, UUID alvoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

        if (matchRepository.existsByUsuario1AndUsuario2(usuario, alvo)) {
            throw new BusinessException("Solicitação já enviada");
        }

        int afinidade = matchmakingService.calcularAfinidade(usuario, alvo);

        Match match = new Match();
        match.setUsuario1(usuario);
        match.setUsuario2(alvo);
        match.setNivelAfinidade(afinidade);
        match.setStatus("PENDENTE");

        return matchRepository.save(match);
    }

    @Transactional
    public Match responderSolicitacao(UUID matchId, String status) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException("Match não encontrado"));

        match.setStatus(status);
        match.setDataResposta(LocalDateTime.now());

        return matchRepository.save(match);
    }

    @Transactional(readOnly = true)
    public List<Match> getMeusMatches(UUID usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return matchRepository.findMatchesByUsuario(usuario, "ACEITO");
    }

    public Usuario getUsuarioById(UUID id) {
    return usuarioRepository.findById(id)
        .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }
}
