package com.guildasocial.api.controller;

import com.guildasocial.api.dto.request.MatchRequestDTO;
import com.guildasocial.api.dto.response.MatchResponseDTO;
import com.guildasocial.api.dto.response.SugestaoResponseDTO;
import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.service.MatchService;
import com.guildasocial.domain.service.MatchmakingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchmakingService matchmakingService;

    public MatchController(MatchService matchService, MatchmakingService matchmakingService) {
        this.matchService = matchService;
        this.matchmakingService = matchmakingService;
    }

    @GetMapping("/sugestoes/{usuarioId}")
    public ResponseEntity<List<SugestaoResponseDTO>> getSugestoes(@PathVariable UUID usuarioId) {
        List<Usuario> sugestoes = matchService.getSugestoes(usuarioId);
        Usuario usuarioAtual = matchService.getUsuarioById(usuarioId);

        List<SugestaoResponseDTO> response = sugestoes.stream()
            .map(usuario -> {
                int afinidade = matchmakingService.calcularAfinidade(usuarioAtual, usuario);
                SugestaoResponseDTO dto = new SugestaoResponseDTO();
                dto.setId(usuario.getId());
                dto.setNome(usuario.getNome());
                dto.setCidade(usuario.getCidade());
                dto.setEstado(usuario.getEstado());
                dto.setDescricao(usuario.getDescricao());
                dto.setFotoPerfil(usuario.getFotoPerfil());
                dto.setNivelAfinidade(afinidade);
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

   @PostMapping("/solicitar/{usuarioId}")
    public ResponseEntity<MatchResponseDTO> enviarSolicitacao(
            @PathVariable UUID usuarioId,
            @RequestBody MatchRequestDTO request) {

        System.out.println("Recebendo solicitação de match");
        System.out.println("Remetente ID: " + usuarioId);
        System.out.println("Alvo ID: " + request.getAlvoId());

        Match match = matchService.enviarSolicitacao(usuarioId, request.getAlvoId());

        MatchResponseDTO response = new MatchResponseDTO();
        response.setId(match.getId());
        response.setUsuarioId(match.getUsuario2().getId());
        response.setUsuarioNome(match.getUsuario2().getNome());
        response.setNivelAfinidade(match.getNivelAfinidade());
        response.setStatus(match.getStatus());
        response.setDataMatch(match.getDataMatch());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/responder/{matchId}")
    public ResponseEntity<MatchResponseDTO> responderSolicitacao(
            @PathVariable UUID matchId, @RequestParam String status) {
        Match match = matchService.responderSolicitacao(matchId, status);

        MatchResponseDTO response = new MatchResponseDTO();
        response.setId(match.getId());
        response.setStatus(match.getStatus());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/meus-matches/{usuarioId}")
    public ResponseEntity<List<MatchResponseDTO>> getMeusMatches(@PathVariable UUID usuarioId) {
        List<Match> matches = matchService.getMeusMatches(usuarioId);

        List<MatchResponseDTO> response = matches.stream()
            .map(match -> {
                MatchResponseDTO dto = new MatchResponseDTO();
                dto.setId(match.getId());
                dto.setUsuarioId(match.getUsuario2().getId());
                dto.setUsuarioNome(match.getUsuario2().getNome());
                dto.setNivelAfinidade(match.getNivelAfinidade());
                dto.setStatus(match.getStatus());
                dto.setDataMatch(match.getDataMatch());
                return dto;
            })
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
