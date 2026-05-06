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
import java.util.Optional;
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
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

        // Verificar se já existe match
        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(usuarioId, alvoId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();

            // Se existe solicitação PENDENTE do outro usuário, aceitar automaticamente (match mútuo)
            if ("PENDENTE".equals(match.getStatus())) {
                // Verificar se o PENDENTE foi enviado pelo alvo (não pelo atual)
                if (match.getUsuario1().getId().equals(alvoId) || match.getUsuario2().getId().equals(alvoId)) {
                    match.setStatus("ACEITO");
                    match.setDataResposta(LocalDateTime.now());
                    return matchRepository.save(match);
                }
            }
            throw new BusinessException("Solicitação já processada");
        }

        // Criar nova solicitação (PENDENTE)
        Match match = new Match();
        match.setUsuario1(usuario);
        match.setUsuario2(alvo);
        match.setNivelAfinidade(matchmakingService.calcularAfinidade(usuario, alvo));
        match.setStatus("PENDENTE");
        match.setDataMatch(LocalDateTime.now());

        return matchRepository.save(match);
    }

    @Transactional
    public Match aceitarSolicitacao(UUID matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new BusinessException("Match não encontrado"));

        match.setStatus("ACEITO");
        match.setDataResposta(LocalDateTime.now());

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

    // Método para passar um usuário (não sugerir novamente)
    @Transactional
    public Match passarSugestao(UUID usuarioId, UUID alvoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

        // Verificar se já existe match
        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(usuarioId, alvoId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();
            if ("PENDENTE".equals(match.getStatus())) {
                match.setStatus("PASSADO");
                return matchRepository.save(match);
            }
            throw new BusinessException("Não é possível passar este usuário");
        }

        // Criar novo match como passado
        Match match = new Match();
        match.setUsuario1(usuario);
        match.setUsuario2(alvo);
        match.setNivelAfinidade(0);
        match.setStatus("PASSADO");
        match.setDataMatch(LocalDateTime.now());
        match.setDataResposta(LocalDateTime.now());

        return matchRepository.save(match);
    }
}
