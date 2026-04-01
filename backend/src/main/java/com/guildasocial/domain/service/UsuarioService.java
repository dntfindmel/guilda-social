package com.guildasocial.domain.service;

import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.api.exception.ResourceNotFoundException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    // REMOVER: private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Transactional
    public Usuario criarUsuario(Usuario usuario, String senhaRaw) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        
        // Salvar senha em texto puro (SEM CRIPTOGRAFIA)
        usuario.setSenha(senhaRaw);
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        
        System.out.println("Usuário criado: ID=" + usuarioSalvo.getId() + ", Email=" + usuarioSalvo.getEmail());
        System.out.println("Senha salva (texto puro): " + usuarioSalvo.getSenha());
        return usuarioSalvo;
    }
    
    // ... resto dos métodos (buscarPorId, listarTodos, etc.)
}