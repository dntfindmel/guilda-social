package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.MatchRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MatchmakingService {

    private final UsuarioRepository usuarioRepository;
    private final MatchRepository matchRepository;

    public MatchmakingService(UsuarioRepository usuarioRepository, MatchRepository matchRepository) {
        this.usuarioRepository = usuarioRepository;
        this.matchRepository = matchRepository;
    }

    public List<Usuario> getSugestoes(UUID usuarioId) {
        Usuario usuarioAtual = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuarioAtual == null) return List.of();

        int raioMaximo = usuarioAtual.getDistanciaMaximaKm() != null ? usuarioAtual.getDistanciaMaximaKm() : 20;

        List<Usuario> todosUsuarios = usuarioRepository.findAllAtivos(usuarioId);

        System.out.println("=== MatchmakingService.getSugestoes ===");
        System.out.println("Usuário: " + usuarioAtual.getNome());
        System.out.println("Raio máximo: " + raioMaximo + "km");
        System.out.println("Total de usuários ativos: " + todosUsuarios.size());

        List<Usuario> sugestoes = todosUsuarios.stream()
            .filter(u -> !u.getId().equals(usuarioId))
            .filter(u -> {
                boolean passado = matchRepository.existeMatchPassado(usuarioId, u.getId());
                if (passado) System.out.println("  - " + u.getNome() + ": PASSADO");
                return !passado;
            })
            .filter(u -> {
                boolean solicitado = matchRepository.existeSolicitacaoEnviada(usuarioId, u.getId());
                if (solicitado) System.out.println("  - " + u.getNome() + ": SOLICITAÇÃO ENVIADA");
                return !solicitado;
            })
            .filter(u -> {
                boolean aceito = matchRepository.existeMatchAceito(usuarioId, u.getId());
                if (aceito) System.out.println("  - " + u.getNome() + ": JÁ É MATCH ACEITO");
                return !aceito;
            })
            .filter(u -> {
                double distancia = calcularDistancia(usuarioAtual, u);
                boolean dentroRaio = distancia <= raioMaximo;
                if (!dentroRaio) System.out.println("  - " + u.getNome() + ": FORA DO RAIO (" + String.format("%.2f", distancia) + "km)");
                return dentroRaio;
            })
            .collect(Collectors.toList());

        System.out.println("Total de sugestões: " + sugestoes.size());
        for (Usuario u : sugestoes) {
            System.out.println("  ✅ " + u.getNome() + " - " + u.getCidade() + "/" + u.getEstado());
        }

        return sugestoes;
    }

    private double calcularDistancia(Usuario u1, Usuario u2) {
        if (u1.getLatitude() == null || u2.getLatitude() == null) return 100;

        double lat1 = u1.getLatitude();
        double lon1 = u1.getLongitude();
        double lat2 = u2.getLatitude();
        double lon2 = u2.getLongitude();

        double theta = lon1 - lon2;
        double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
                      Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
        dist = Math.acos(dist);
        dist = Math.toDegrees(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist;
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

        return Math.min(afinidade, 100);
    }
}
