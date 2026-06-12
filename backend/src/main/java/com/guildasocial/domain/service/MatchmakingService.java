package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.MatchRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        if (usuarioAtual == null) return new ArrayList<>();

        int raioMaximo = usuarioAtual.getDistanciaMaximaKm() != null ? usuarioAtual.getDistanciaMaximaKm() : 20;

        List<Usuario> todosUsuarios = usuarioRepository.findAllAtivos(usuarioId);

        System.out.println("=== MatchmakingService.getSugestoes ===");
        System.out.println("Usuário atual: " + usuarioAtual.getNome());
        System.out.println("Raio máximo: " + raioMaximo + "km");

        List<Usuario> sugestoes = new ArrayList<>();

        for (Usuario u : todosUsuarios) {
            if (u.getId().equals(usuarioId)) continue;

            double distancia = calcularDistancia(usuarioAtual, u);

            // Verificar interações do USUÁRIO ATUAL com o alvo
            boolean passadoPeloAtual = matchRepository.existeMatchPassado(usuarioId, u.getId());
            boolean solicitadoPeloAtual = matchRepository.existeSolicitacaoEnviada(usuarioId, u.getId());
            boolean aceito = matchRepository.existeMatchAceito(usuarioId, u.getId());

            // Verificar se o ALVO passou o atual (NÃO deve bloquear)
            boolean alvoPassouAtual = matchRepository.existeMatchPassado(u.getId(), usuarioId);

            System.out.println("Verificando: " + u.getNome());
            System.out.println("  Passado pelo atual: " + passadoPeloAtual);
            System.out.println("  Solicitado pelo atual: " + solicitadoPeloAtual);
            System.out.println("  Aceito: " + aceito);
            System.out.println("  Alvo passou atual: " + alvoPassouAtual);
            System.out.println("  Distância: " + String.format("%.2f", distancia) + "km");

            // ⚠️ REGRA: Só bloqueia se o USUÁRIO ATUAL passou OU solicitou OU já tem match ACEITO
            // Não bloqueia se o ALVO passou o atual (permite que o atual solicite)
            if (!passadoPeloAtual && !solicitadoPeloAtual && !aceito && distancia <= raioMaximo) {
                System.out.println("  ✅ ADICIONADO");
                sugestoes.add(u);
            } else {
                System.out.println("  ❌ REMOVIDO");
            }
        }

        System.out.println("Total de sugestões: " + sugestoes.size());
        return sugestoes;
    }

    private double calcularDistancia(Usuario u1, Usuario u2) {
        if (u1.getLatitude() == null || u2.getLatitude() == null) return 1000;

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
