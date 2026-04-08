package com.guildasocial.domain.service;

import com.guildasocial.domain.model.PerfilJogador;
import com.guildasocial.domain.model.PreferenciaJogo;
import com.guildasocial.domain.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class MatchmakingService {

    public int calcularAfinidade(Usuario usuario1, Usuario usuario2) {
        int afinidade = 0;
        int pesoTotal = 0;

        // 1. Compatibilidade de jogos (peso 3)
        pesoTotal += 3;
        afinidade += calcularCompatibilidadeJogos(usuario1, usuario2) * 3;

        // 2. Compatibilidade de estilo (peso 2)
        pesoTotal += 2;
        afinidade += calcularCompatibilidadeEstilo(usuario1, usuario2) * 2;

        // 3. Compatibilidade de localização (peso 1)
        pesoTotal += 1;
        afinidade += calcularCompatibilidadeLocalizacao(usuario1, usuario2) * 1;

        return pesoTotal > 0 ? afinidade / pesoTotal : 0;
    }

    private int calcularCompatibilidadeJogos(Usuario u1, Usuario u2) {
        // Simulação: verificar se têm jogos em comum
        // Aqui você implementaria a lógica real baseada nas preferências
        return 70; // Percentual de compatibilidade
    }

    private int calcularCompatibilidadeEstilo(Usuario u1, Usuario u2) {
        // Simulação: comparar estilos de jogo
        return 65;
    }

    private int calcularCompatibilidadeLocalizacao(Usuario u1, Usuario u2) {
        // Simulação: comparar proximidade geográfica
        if (u1.getCidade() != null && u2.getCidade() != null &&
            u1.getCidade().equalsIgnoreCase(u2.getCidade())) {
            return 100;
        }
        return 50;
    }
}
