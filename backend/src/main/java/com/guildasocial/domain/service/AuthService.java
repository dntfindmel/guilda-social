package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.LoginRequestDTO;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

// AuthService.java - método autenticar

  public LoginResponseDTO autenticar(LoginRequestDTO request) {
      System.out.println("=========================================");
      System.out.println("=== INICIANDO LOGIN ===");
      System.out.println("Email recebido: [" + request.getEmail() + "]");
      System.out.println("Senha recebida: [" + request.getSenha() + "]");
      
      // Buscar usuário
      Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
              .orElse(null);
      
      if (usuario == null) {
          System.out.println("❌ Usuário NÃO encontrado para o email: " + request.getEmail());
          throw new BusinessException("E-mail ou senha inválidos");
      }
      
      System.out.println("✅ Usuário encontrado:");
      System.out.println("   ID: " + usuario.getId());
      System.out.println("   Email: " + usuario.getEmail());
      System.out.println("   Senha no banco: [" + usuario.getSenha() + "]");
      
      // Comparar senhas
      if (!usuario.getSenha().equals(request.getSenha())) {
          System.out.println("❌ Senha NÃO confere!");
          System.out.println("   Senha informada: [" + request.getSenha() + "]");
          System.out.println("   Senha no banco: [" + usuario.getSenha() + "]");
          throw new BusinessException("E-mail ou senha inválidos");
      }
      
      System.out.println("✅ Senha confere!");
      System.out.println("=========================================");
      
      // Gerar token
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
      );
      
      SecurityContextHolder.getContext().setAuthentication(authentication);
      String token = tokenProvider.generateToken(authentication);
      
      return new LoginResponseDTO(token, usuario.getId(), usuario.getNome(), usuario.getEmail());
  }
    
}