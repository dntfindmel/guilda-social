package com.guildasocial.api.controller;

import com.guildasocial.api.dto.request.CadastroRequestDTO;
import com.guildasocial.api.dto.response.UsuarioResponseDTO;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cadastra um novo usuário na plataforma")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                     content = @Content(schema = @Schema(implementation = UsuarioResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "409", description = "E-mail já cadastrado", content = @Content)
    })
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody CadastroRequestDTO request) {
        log.debug("Recebendo requisição de cadastro para e-mail: {}", request.getEmail());

        Usuario usuario = modelMapper.map(request, Usuario.class);
        Usuario usuarioCriado = usuarioService.criarUsuario(usuario, request.getSenha());

        UsuarioResponseDTO response = modelMapper.map(usuarioCriado, UsuarioResponseDTO.class);

        log.info("Usuário criado com sucesso: ID={}, Email={}", usuarioCriado.getId(), usuarioCriado.getEmail());

        return ResponseEntity
            .created(URI.create("/api/usuarios/" + usuarioCriado.getId()))
            .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        log.debug("Buscando usuário por ID: {}", id);

        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários ativos")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(
            @RequestParam(required = false) UUID usuarioId) {

        log.debug("Listando usuários ativos");

        List<Usuario> usuarios = usuarioService.listarTodos(usuarioId);

        List<UsuarioResponseDTO> response = usuarios.stream()
            .map(usuario -> {
                UsuarioResponseDTO dto = modelMapper.map(usuario, UsuarioResponseDTO.class);
                return dto;
            })
            .toList();

        log.info("Total de usuários retornados: {}", response.size());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody CadastroRequestDTO request) {

        log.debug("Atualizando usuário ID: {}", id);

        Usuario usuarioAtualizado = modelMapper.map(request, Usuario.class);
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);

        UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);

        log.info("Usuário atualizado com sucesso: ID={}", id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário desativado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        log.debug("Desativando usuário ID: {}", id);

        usuarioService.deletarUsuario(id);

        log.info("Usuário desativado com sucesso: ID={}", id);

        return ResponseEntity.noContent().build();
    }
}
