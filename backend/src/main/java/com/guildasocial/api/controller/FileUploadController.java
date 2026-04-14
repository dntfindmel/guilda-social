package com.guildasocial.api.controller;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.api.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    private final UsuarioRepository usuarioRepository;
    private final String UPLOAD_DIR = "uploads/";

    public FileUploadController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        // Criar diretório se não existir
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/foto/{usuarioId}")
    public ResponseEntity<Map<String, String>> uploadFoto(
            @PathVariable UUID usuarioId,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

            // Gerar nome único para o arquivo
            String fileName = usuarioId.toString() + "_" + System.currentTimeMillis() + ".jpg";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Salvar arquivo
            Files.write(filePath, file.getBytes());

            // Salvar caminho no banco
            String fotoUrl = "/uploads/" + fileName;
            usuario.setFotoPerfil(fotoUrl);
            usuarioRepository.save(usuario);

            response.put("fotoUrl", fotoUrl);
            response.put("message", "Foto atualizada com sucesso!");

            System.out.println("Foto salva para usuário " + usuarioId + ": " + fotoUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("error", "Erro ao salvar foto: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @DeleteMapping("/foto/{usuarioId}")
    public ResponseEntity<Map<String, String>> removerFoto(@PathVariable UUID usuarioId) {
        Map<String, String> response = new HashMap<>();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setFotoPerfil(null);
        usuarioRepository.save(usuario);

        response.put("message", "Foto removida com sucesso!");
        return ResponseEntity.ok(response);
    }
}
