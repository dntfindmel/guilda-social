package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Mensagem;
import com.guildasocial.domain.repository.MatchRepository;
import com.guildasocial.domain.repository.MensagemRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final MensagemRepository mensagemRepository;
    private final MatchRepository matchRepository;
    private final UsuarioRepository usuarioRepository;

    public ChatService(MensagemRepository mensagemRepository,
                       MatchRepository matchRepository,
                       UsuarioRepository usuarioRepository) {
        this.mensagemRepository = mensagemRepository;
        this.matchRepository = matchRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Mensagem> getMensagens(UUID matchId) {
        return mensagemRepository.findByMatchIdOrderByDataEnvioAsc(matchId);
    }

    @Transactional
    public Mensagem enviarMensagem(UUID matchId, UUID remetenteId, String conteudo) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match não encontrado: " + matchId));

        Mensagem mensagem = new Mensagem();
        mensagem.setMatch(match);
        mensagem.setRemetenteId(remetenteId);
        mensagem.setConteudo(conteudo);
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setLida(false);

        return mensagemRepository.save(mensagem);
    }

    public List<Match> getMatchesComUltimaMensagem(UUID usuarioId) {
        var usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return new ArrayList<>();

        List<Match> matches1 = matchRepository.findByUsuario1AndStatus(usuario, "ACEITO");
        List<Match> matches2 = matchRepository.findByUsuario2AndStatus(usuario, "ACEITO");

        List<Match> allMatches = new ArrayList<>();
        allMatches.addAll(matches1);
        allMatches.addAll(matches2);

        // Para cada match, buscar apenas a última mensagem (sem carregar a lista completa)
        for (Match match : allMatches) {
            try {
                List<Mensagem> mensagens = mensagemRepository.findByMatchIdOrderByDataEnvioAsc(match.getId());
                if (!mensagens.isEmpty()) {
                    // Criar uma nova instância apenas com os dados necessários
                    Mensagem ultima = mensagens.get(mensagens.size() - 1);
                    // Criar uma cópia para evitar o loop
                    Mensagem copia = new Mensagem();
                    copia.setId(ultima.getId());
                    copia.setConteudo(ultima.getConteudo());
                    copia.setDataEnvio(ultima.getDataEnvio());
                    copia.setRemetenteId(ultima.getRemetenteId());
                    copia.setLida(ultima.getLida());
                    match.setUltimaMensagem(copia);
                } else {
                    match.setUltimaMensagem(null);
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar mensagem para match " + match.getId() + ": " + e.getMessage());
                match.setUltimaMensagem(null);
            }
        }

        return allMatches;
    }
}
