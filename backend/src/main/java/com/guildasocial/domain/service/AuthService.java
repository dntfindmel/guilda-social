package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.LoginRequestDTO;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = tokenProvider.generateToken(authentication);

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

            return LoginResponseDTO.builder()
                    .token(token)
                    .usuarioId(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .build();

        } catch (Exception e) {
            throw new BusinessException("E-mail ou senha inválidos");
        }
    }
}
