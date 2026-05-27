package com.guildasocial.api.controller;

import com.guildasocial.api.dto.request.*;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<Map<String, String>> recuperarSenha(@RequestBody RecuperarSenhaRequestDTO request) {
        try {
            authService.solicitarRecuperacaoSenha(request.getEmail());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Link de recuperação enviado para o e-mail");
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/redefinir-senha")
    public ResponseEntity<Map<String, String>> redefinirSenha(@RequestBody RedefinirSenhaRequestDTO request) {
        try {
            authService.redefinirSenha(request.getToken(), request.getNovaSenha());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Senha redefinida com sucesso");
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/alterar-senha/{usuarioId}")
    public ResponseEntity<Map<String, String>> alterarSenha(@PathVariable UUID usuarioId, @RequestBody AlterarSenhaRequestDTO request) {
        System.out.println("=== Alterar Senha ===");
        System.out.println("Usuário ID: " + usuarioId);
        System.out.println("Senha atual: " + request.getSenhaAntiga());
        System.out.println("Nova senha: " + request.getNovaSenha());

        try {
            authService.alterarSenha(usuarioId, request.getSenhaAntiga(), request.getNovaSenha());
            Map<String, String> response = new HashMap<>();
            response.put("message", "Senha alterada com sucesso");
            return ResponseEntity.ok(response);
        } catch (BusinessException e) {
            System.err.println("Erro ao alterar senha: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
