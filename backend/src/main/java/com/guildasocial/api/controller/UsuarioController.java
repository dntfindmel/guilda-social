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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;
    private final String UPLOAD_DIR = "uploads/";

    public UsuarioController(UsuarioService usuarioService, ModelMapper modelMapper) {
        this.usuarioService = usuarioService;
        this.modelMapper = modelMapper;
        // Criar diretório se não existir
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody CadastroRequestDTO request) {
        Usuario usuario = modelMapper.map(request, Usuario.class);

        if (request.getFotoPerfil() != null && !request.getFotoPerfil().isEmpty()) {
            String fotoUrl = salvarFotoBase64(request.getFotoPerfil(), null);
            usuario.setFotoPerfil(fotoUrl);
        }

        Usuario usuarioCriado = usuarioService.criarUsuario(usuario, request.getSenha());

        UsuarioResponseDTO response = modelMapper.map(usuarioCriado, UsuarioResponseDTO.class);

        return ResponseEntity
            .created(URI.create("/api/usuarios/" + usuarioCriado.getId()))
            .body(response);
    }

    private String salvarFotoBase64(String base64, UUID usuarioId) {
        try {
            // Remover prefixo se existir (data:image/png;base64,)
            String base64Data = base64;
            if (base64.contains(",")) {
                base64Data = base64.split(",")[1];
            }

            byte[] imageBytes = java.util.Base64.getDecoder().decode(base64Data);

            String fileName = (usuarioId != null ? usuarioId.toString() : UUID.randomUUID().toString()) + "_" + System.currentTimeMillis() + ".jpg";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            Files.write(filePath, imageBytes);

            return "/uploads/" + fileName;

        } catch (Exception e) {
            System.err.println("Erro ao salvar foto: " + e.getMessage());
            return null;
        }
    }
}
