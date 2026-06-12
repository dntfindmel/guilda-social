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

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
        System.out.println("=== GET /sugestoes/" + usuarioId + " ===");

        List<Usuario> sugestoes = matchService.getSugestoes(usuarioId);
        Usuario usuarioAtual = matchService.getUsuarioById(usuarioId);

        List<SugestaoResponseDTO> response = new ArrayList<>();

        for (Usuario usuario : sugestoes) {
            int afinidade = matchmakingService.calcularAfinidade(usuarioAtual, usuario);
            double distancia = calcularDistancia(usuarioAtual, usuario);
            int idade = calcularIdade(usuario.getDataNascimento());

            SugestaoResponseDTO dto = new SugestaoResponseDTO();
            dto.setId(usuario.getId());
            dto.setNome(usuario.getNome());
            dto.setCidade(usuario.getCidade());
            dto.setEstado(usuario.getEstado());
            dto.setDescricao(usuario.getDescricao());
            dto.setFotoPerfil(usuario.getFotoPerfil());
            dto.setNivelAfinidade(afinidade);
            dto.setNivel((int) (Math.random() * 60) + 20);
            dto.setDistancia((int) Math.round(distancia));
            dto.setIdade(idade);
            dto.setTags(new String[]{"MMORPG", "Voice Chat", "Late Night"});

            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/solicitar/{usuarioId}")
    public ResponseEntity<MatchResponseDTO> enviarSolicitacao(
            @PathVariable UUID usuarioId,
            @RequestBody MatchRequestDTO request) {
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

    @PostMapping("/passar/{usuarioId}")
    public ResponseEntity<Void> passarSugestao(
            @PathVariable UUID usuarioId,
            @RequestBody MatchRequestDTO request) {
        matchService.passarSugestao(usuarioId, request.getAlvoId());
        return ResponseEntity.ok().build();
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

        List<MatchResponseDTO> response = new ArrayList<>();
        for (Match match : matches) {
            MatchResponseDTO dto = new MatchResponseDTO();
            dto.setId(match.getId());
            dto.setUsuarioId(match.getUsuario2().getId());
            dto.setUsuarioNome(match.getUsuario2().getNome());
            dto.setNivelAfinidade(match.getNivelAfinidade());
            dto.setStatus(match.getStatus());
            dto.setDataMatch(match.getDataMatch());
            response.add(dto);
        }

        return ResponseEntity.ok(response);
    }

    private double calcularDistancia(Usuario u1, Usuario u2) {
        if (u1.getLatitude() == null || u2.getLatitude() == null) return 20;

        double lat1 = u1.getLatitude();
        double lon1 = u1.getLongitude();
        double lat2 = u2.getLatitude();
        double lon2 = u2.getLongitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(Math.min(1, Math.max(-1, dist)));
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist;
    }

    private int calcularIdade(Date dataNascimento) {
        if (dataNascimento == null) return 25;

        try {
            LocalDate nascimento = dataNascimento.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            LocalDate hoje = LocalDate.now();
            return Period.between(nascimento, hoje).getYears();
        } catch (Exception e) {
            System.err.println("Erro ao calcular idade: " + e.getMessage());
            return 25;
        }
    }
}
