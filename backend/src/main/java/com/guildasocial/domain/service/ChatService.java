package com.guildasocial.domain.service;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Mensagem;
import com.guildasocial.domain.model.Usuario;
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
                .orElseThrow(() -> new RuntimeException("Match não encontrado"));

        Mensagem mensagem = new Mensagem();
        mensagem.setMatch(match);
        mensagem.setRemetenteId(remetenteId);
        mensagem.setConteudo(conteudo);
        mensagem.setDataEnvio(LocalDateTime.now());
        mensagem.setLida(false);

        return mensagemRepository.save(mensagem);
    }

    public List<Match> getMatchesComUltimaMensagem(UUID usuarioId) {
        // Validar parâmetro
        if (usuarioId == null) {
            return new ArrayList<>();
        }

        // Buscar usuário
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElse(null);

        if (usuario == null) {
            return new ArrayList<>();
        }

        // Buscar matches onde o usuário é usuario1 OU usuario2 com status ACEITO
        List<Match> matches1 = matchRepository.findByUsuario1AndStatus(usuario, "ACEITO");
        List<Match> matches2 = matchRepository.findByUsuario2AndStatus(usuario, "ACEITO");

        // Combinar as duas listas
        List<Match> allMatches = new ArrayList<>();
        allMatches.addAll(matches1);
        allMatches.addAll(matches2);

        // Para cada match, buscar a última mensagem
        for (Match match : allMatches) {
            Mensagem ultima = mensagemRepository.findTopByMatchOrderByDataEnvioDesc(match);
            match.setUltimaMensagem(ultima);
        }

        return allMatches;
    }
}
