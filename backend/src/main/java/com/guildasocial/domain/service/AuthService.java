package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.LoginRequestDTO;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.security.JwtTokenProvider;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    public AuthService(JwtTokenProvider tokenProvider,
                       UsuarioRepository usuarioRepository) {
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        System.out.println("=== INICIANDO LOGIN ===");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Senha: " + request.getSenha());

        try {
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

        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("E-mail ou senha inválidos");
        }
    }
}
