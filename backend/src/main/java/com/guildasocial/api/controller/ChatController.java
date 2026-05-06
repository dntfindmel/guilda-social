package com.guildasocial.api.controller;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Mensagem;
import com.guildasocial.domain.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/mensagens/{matchId}")
    public ResponseEntity<List<Mensagem>> getMensagens(@PathVariable UUID matchId) {
        return ResponseEntity.ok(chatService.getMensagens(matchId));
    }

    @PostMapping("/mensagens/{matchId}")
    public ResponseEntity<Mensagem> enviarMensagem(
            @PathVariable UUID matchId,
            @RequestBody MensagemRequest request) {
        Mensagem mensagem = chatService.enviarMensagem(matchId, request.getRemetenteId(), request.getConteudo());
        return ResponseEntity.ok(mensagem);
    }

    @GetMapping("/matches/{usuarioId}")
    public ResponseEntity<List<Match>> getMatchesComChat(@PathVariable UUID usuarioId) {
        List<Match> matches = chatService.getMatchesComUltimaMensagem(usuarioId);
        return ResponseEntity.ok(matches);
    }
}

class MensagemRequest {
    private UUID remetenteId;
    private String conteudo;

    public UUID getRemetenteId() { return remetenteId; }
    public void setRemetenteId(UUID remetenteId) { this.remetenteId = remetenteId; }
    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }
}
