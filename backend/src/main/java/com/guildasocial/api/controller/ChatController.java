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

    @GetMapping("/matches/{usuarioId}")
    public ResponseEntity<List<Match>> getMatchesComChat(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(chatService.getMatchesComUltimaMensagem(usuarioId));
    }

    @GetMapping("/mensagens/{matchId}")
    public ResponseEntity<List<Mensagem>> getMensagens(@PathVariable UUID matchId) {
        return ResponseEntity.ok(chatService.getMensagens(matchId));
    }

    @PostMapping("/mensagens/{matchId}")
    public ResponseEntity<Mensagem> enviarMensagem(
            @PathVariable UUID matchId,
            @RequestParam UUID remetenteId,
            @RequestParam String conteudo) {
        if (matchId == null || remetenteId == null || conteudo == null || conteudo.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(chatService.enviarMensagem(matchId, remetenteId, conteudo));
    }
}
