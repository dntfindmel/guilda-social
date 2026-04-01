package com.guildasocial.security;

import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

// UserDetailsServiceImpl.java
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      System.out.println("=== loadUserByUsername ===");
      System.out.println("Email recebido: [" + email + "]");
      
      Usuario usuario = usuarioRepository.findByEmail(email)
              .orElseThrow(() -> {
                  System.out.println("❌ Usuário não encontrado!");
                  return new UsernameNotFoundException("Usuário não encontrado: " + email);
              });
      
      System.out.println("✅ Usuário encontrado: " + usuario.getEmail());
      System.out.println("   Senha no banco: [" + usuario.getSenha() + "]");
      System.out.println("=========================================");
      
      return new User(usuario.getEmail(), usuario.getSenha(), new ArrayList<>());
  }
}