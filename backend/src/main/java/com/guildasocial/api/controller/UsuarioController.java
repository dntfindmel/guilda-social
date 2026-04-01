package com.guildasocial.api.controller;

import com.guildasocial.api.dto.request.CadastroRequestDTO;
import com.guildasocial.api.dto.response.UsuarioResponseDTO;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;

    // Construtor explícito
    public UsuarioController(UsuarioService usuarioService, ModelMapper modelMapper) {
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody CadastroRequestDTO request) {
        Usuario usuario = modelMapper.map(request, Usuario.class);
        Usuario usuarioCriado = usuarioService.criarUsuario(usuario, request.getSenha());
        
        UsuarioResponseDTO response = modelMapper.map(usuarioCriado, UsuarioResponseDTO.class);
        
        return ResponseEntity
            .created(URI.create("/api/usuarios/" + usuarioCriado.getId()))
            .body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable UUID id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários ativos")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos(@RequestParam(required = false) UUID usuarioId) {
        List<Usuario> usuarios = usuarioService.listarTodos(usuarioId);
        
        List<UsuarioResponseDTO> response = usuarios.stream()
            .map(usuario -> modelMapper.map(usuario, UsuarioResponseDTO.class))
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable UUID id,
            @Valid @RequestBody CadastroRequestDTO request) {
        
        Usuario usuarioAtualizado = modelMapper.map(request, Usuario.class);
        Usuario usuario = usuarioService.atualizarUsuario(id, usuarioAtualizado);
        
        UsuarioResponseDTO response = modelMapper.map(usuario, UsuarioResponseDTO.class);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar usuário")
    public ResponseEntity<Void> deletarUsuario(@PathVariable UUID id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}