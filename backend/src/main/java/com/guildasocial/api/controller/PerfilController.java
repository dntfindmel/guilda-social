package com.guildasocial.api.controller;

import com.guildasocial.api.dto.request.AtualizarPerfilRequestDTO;
import com.guildasocial.api.dto.response.PerfilResponseDTO;
import com.guildasocial.domain.service.PerfilService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perfil")
@Tag(name = "Perfil", description = "Endpoints para gerenciamento de perfil")
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @GetMapping("/{usuarioId}")
    @Operation(summary = "Buscar perfil do usuário")
    public ResponseEntity<PerfilResponseDTO> buscarPerfil(@PathVariable UUID usuarioId) {
        PerfilResponseDTO perfil = perfilService.buscarPerfil(usuarioId);
        return ResponseEntity.ok(perfil);
    }

    @PutMapping("/{usuarioId}")
    @Operation(summary = "Atualizar perfil do usuário")
    public ResponseEntity<PerfilResponseDTO> atualizarPerfil(
            @PathVariable UUID usuarioId,
            @Valid @RequestBody AtualizarPerfilRequestDTO request) {
        PerfilResponseDTO perfil = perfilService.atualizarPerfil(usuarioId, request);
        return ResponseEntity.ok(perfil);
    }
}
