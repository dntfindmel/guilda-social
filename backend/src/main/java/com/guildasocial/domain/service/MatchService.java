package com.guildasocial.domain.service;

import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.MatchRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
        System.out.println("=== MatchService.getSugestoes ===");
        System.out.println("Usuário ID: " + usuarioId);

        List<Usuario> sugestoes = matchmakingService.getSugestoes(usuarioId);

        System.out.println("Total de sugestões encontradas: " + sugestoes.size());
        for (Usuario u : sugestoes) {
            System.out.println("  - " + u.getNome() + " (" + u.getEmail() + ")");
        }

        return sugestoes;
    }

    public Usuario getUsuarioById(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

@Transactional
public Match enviarSolicitacao(UUID usuarioId, UUID alvoId) {
    System.out.println("=== enviarSolicitacao ===");
    System.out.println("usuarioId: " + usuarioId);
    System.out.println("alvoId: " + alvoId);

    // Validar se o alvo é diferente do remetente
    if (usuarioId.equals(alvoId)) {
        throw new BusinessException("Não é possível enviar solicitação para si mesmo");
    }

    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    Usuario alvo = usuarioRepository.findById(alvoId)
            .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

    // Verificar se já existe match
    if (matchRepository.existsByUsuario1AndUsuario2(usuario, alvo)) {
        throw new BusinessException("Solicitação já enviada");
    }

    int afinidade = matchmakingService.calcularAfinidade(usuario, alvo);

    Match match = new Match();
    match.setUsuario1(usuario);
    match.setUsuario2(alvo);
    match.setNivelAfinidade(afinidade);
    match.setStatus("ACEITO"); // Aceitar automaticamente para teste
    match.setDataMatch(LocalDateTime.now());
    match.setDataResposta(LocalDateTime.now());

    System.out.println("Match criado com sucesso!");

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

    @Transactional(readOnly = true)
    public List<Match> getTodosMatches(UUID usuarioId) {
        return matchRepository.findAllByUsuarioId(usuarioId);
    }
}
