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
    private static final double RAIO_MAXIMO_KM = 20.0;

    public MatchmakingService(UsuarioRepository usuarioRepository, MatchRepository matchRepository) {
        this.usuarioRepository = usuarioRepository;
        this.matchRepository = matchRepository;
    }

    public List<Usuario> getSugestoes(UUID usuarioId) {
        Usuario usuarioAtual = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuarioAtual == null) return List.of();

        // Buscar todos os usuários ativos (excluindo o próprio)
        List<Usuario> todosUsuarios = usuarioRepository.findAllAtivos(usuarioId);

        return todosUsuarios.stream()
            // Filtrar por distância (até 20km)
            .filter(u -> calcularDistancia(usuarioAtual, u) <= RAIO_MAXIMO_KM)
            // Excluir se o usuário atual já enviou solicitação (PENDENTE)
            .filter(u -> !matchRepository.existeSolicitacaoEnviada(usuarioId, u.getId()))
            // Excluir se o usuário atual já passou
            .filter(u -> !matchRepository.existePassado(usuarioId, u.getId()))
            // Excluir se já são amigos (ACEITO)
            .filter(u -> !matchRepository.existeMatchAceito(usuarioId, u.getId()))
            .collect(Collectors.toList());
    }

    private double calcularDistancia(Usuario u1, Usuario u2) {
        if (u1.getLatitude() == null || u2.getLatitude() == null) return RAIO_MAXIMO_KM + 1;

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
        dist = dist * 1.609344; // Km

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
