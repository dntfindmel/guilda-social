package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.LoginRequestDTO;
import com.guildasocial.api.dto.request.RecuperarSenhaRequestDTO;
import com.guildasocial.api.dto.request.RedefinirSenhaRequestDTO;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    public AuthService(JwtTokenProvider tokenProvider,
                       UsuarioRepository usuarioRepository,
                       EmailService emailService) {
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválidos"));

        if (!usuario.getSenha().equals(request.getSenha())) {
            throw new BusinessException("E-mail ou senha inválidos");
        }

        String token = tokenProvider.generateTokenTest(usuario.getEmail());

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setType("Bearer");
        response.setUsuarioId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());

        return response;
    }

    public void solicitarRecuperacaoSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail não encontrado"));

        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        emailService.enviarEmailRecuperacao(usuario.getEmail(), token);
    }

    public void redefinirSenha(String token, String novaSenha) {
        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new BusinessException("Token inválido"));

        if (usuario.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Token expirado");
        }

        usuario.setSenha(novaSenha);
        usuario.setResetToken(null);
        usuario.setResetTokenExpiry(null);
        usuarioRepository.save(usuario);
    }

    public void alterarSenha(UUID usuarioId, String senhaAntiga, String novaSenha) {
        System.out.println("=== AuthService.alterarSenha ===");
        System.out.println("UsuarioId: " + usuarioId);
        System.out.println("Senha antiga: " + senhaAntiga);
        System.out.println("Nova senha: " + novaSenha);

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    System.err.println("Usuário não encontrado: " + usuarioId);
                    return new BusinessException("Usuário não encontrado");
                });

        System.out.println("Usuário encontrado: " + usuario.getEmail());
        System.out.println("Senha atual no banco: " + usuario.getSenha());

        if (!usuario.getSenha().equals(senhaAntiga)) {
            System.err.println("Senha atual incorreta! Digitada: " + senhaAntiga + " | Banco: " + usuario.getSenha());
            throw new BusinessException("Senha atual incorreta");
        }

        if (novaSenha.length() < 6) {
            throw new BusinessException("Nova senha deve ter no mínimo 6 caracteres");
        }

        usuario.setSenha(novaSenha);
        usuarioRepository.save(usuario);
        System.out.println("Senha alterada com sucesso!");
    }
}
