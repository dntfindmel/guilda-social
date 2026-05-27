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

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario criarUsuario(Usuario usuario, String senhaRaw) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        usuario.setSenha(senhaRaw);

        Usuario usuarioSalvo = usuarioRepository.save(usuario);

        System.out.println("Usuário criado: ID=" + usuarioSalvo.getId() + ", Email=" + usuarioSalvo.getEmail());
        System.out.println("Senha salva (texto puro): " + usuarioSalvo.getSenha());
        return usuarioSalvo;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + email));
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos(UUID usuarioId) {
        return usuarioRepository.findAllAtivos(usuarioId);
    }

    @Transactional
    public Usuario atualizarUsuario(UUID id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        if (usuarioAtualizado.getNome() != null) {
            usuario.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getTelefone() != null) {
            usuario.setTelefone(usuarioAtualizado.getTelefone());
        }
        if (usuarioAtualizado.getCidade() != null) {
            usuario.setCidade(usuarioAtualizado.getCidade());
        }
        if (usuarioAtualizado.getEstado() != null) {
            usuario.setEstado(usuarioAtualizado.getEstado());
        }
        if (usuarioAtualizado.getLatitude() != null) {
            usuario.setLatitude(usuarioAtualizado.getLatitude());
        }
        if (usuarioAtualizado.getLongitude() != null) {
            usuario.setLongitude(usuarioAtualizado.getLongitude());
        }
        if (usuarioAtualizado.getDescricao() != null) {
            usuario.setDescricao(usuarioAtualizado.getDescricao());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        System.out.println("Usuário desativado: ID=" + id);
    }
}
