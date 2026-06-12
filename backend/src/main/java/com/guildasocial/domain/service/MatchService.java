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
        return matchmakingService.getSugestoes(usuarioId);
    }

    public Usuario getUsuarioById(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
    }

    @Transactional
    public Match enviarSolicitacao(UUID usuarioId, UUID alvoId) {
        System.out.println("=== enviarSolicitacao ===");
        System.out.println("Remetente (quem solicita): " + usuarioId);
        System.out.println("Destinatário (alvo): " + alvoId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

        // Verificar se o REMETENTE já passou o DESTINATÁRIO
        boolean passadoPeloRemetente = matchRepository.existeMatchPassado(usuarioId, alvoId);
        System.out.println("Remetente passou destinatário? " + passadoPeloRemetente);
        if (passadoPeloRemetente) {
            throw new BusinessException("Você não pode solicitar chat para um usuário que você passou");
        }

        // Verificar se já existe match ACEITO (já são amigos)
        boolean aceito = matchRepository.existeMatchAceito(usuarioId, alvoId);
        System.out.println("Já existe match aceito? " + aceito);
        if (aceito) {
            throw new BusinessException("Você já tem um match com este usuário");
        }

        // Buscar match existente
        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(usuarioId, alvoId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();
            System.out.println("Match existente - Status: " + match.getStatus());
            System.out.println("Usuario1: " + match.getUsuario1().getId());
            System.out.println("Usuario2: " + match.getUsuario2().getId());

            // TRATAR STATUS PASSADO
            if ("PASSADO".equals(match.getStatus())) {
                System.out.println("Match está como PASSADO");
                // Verificar quem passou quem
                boolean remetentePassou = match.getUsuario1().getId().equals(usuarioId) && "PASSADO".equals(match.getStatus());

                if (remetentePassou) {
                    throw new BusinessException("Você passou este usuário anteriormente");
                } else {
                    // O destinatário passou o remetente - permitir solicitação
                    System.out.println("Destinatário passou o remetente - permitindo solicitação");
                    // Atualizar o match existente para PENDENTE
                    match.setStatus("PENDENTE");
                    match.setDataResposta(null);
                    return matchRepository.save(match);
                }
            }

            // Se o DESTINATÁRIO enviou solicitação pendente, aceitar automaticamente
            if ("PENDENTE".equals(match.getStatus())) {
                // Verificar quem é o remetente da solicitação pendente
                if (match.getUsuario1().getId().equals(alvoId) || match.getUsuario2().getId().equals(alvoId)) {
                    System.out.println("Destinatário já enviou solicitação - Aceitando match");
                    match.setStatus("ACEITO");
                    match.setDataResposta(LocalDateTime.now());
                    return matchRepository.save(match);
                }
            }

            // Se o REMETENTE já enviou solicitação
            if ((match.getUsuario1().getId().equals(usuarioId) || match.getUsuario2().getId().equals(usuarioId))
                    && "PENDENTE".equals(match.getStatus())) {
                throw new BusinessException("Você já enviou solicitação para este usuário");
            }

            throw new BusinessException("Não foi possível processar a solicitação");
        }

        // Criar nova solicitação
        Match match = new Match();
        match.setUsuario1(usuario);
        match.setUsuario2(alvo);
        match.setNivelAfinidade(matchmakingService.calcularAfinidade(usuario, alvo));
        match.setStatus("PENDENTE");
        match.setDataMatch(LocalDateTime.now());

        System.out.println("Nova solicitação criada com sucesso");
        return matchRepository.save(match);
    }

    @Transactional
    public Match passarSugestao(UUID usuarioId, UUID alvoId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Usuario alvo = usuarioRepository.findById(alvoId)
                .orElseThrow(() -> new BusinessException("Usuário alvo não encontrado"));

        Optional<Match> existingMatch = matchRepository.findMatchBetweenUsers(usuarioId, alvoId);

        if (existingMatch.isPresent()) {
            Match match = existingMatch.get();
            match.setStatus("PASSADO");
            match.setDataResposta(LocalDateTime.now());
            return matchRepository.save(match);
        }

        Match match = new Match();
        match.setUsuario1(usuario);
        match.setUsuario2(alvo);
        match.setNivelAfinidade(0);
        match.setStatus("PASSADO");
        match.setDataMatch(LocalDateTime.now());
        match.setDataResposta(LocalDateTime.now());

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
}
