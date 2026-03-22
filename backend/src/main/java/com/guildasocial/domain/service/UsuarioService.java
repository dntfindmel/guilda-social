package com.guildasocial.domain.service;

import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.api.exception.ResourceNotFoundException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Cria um novo usuário com senha criptografada
     * @param usuario dados do usuário
     * @param senhaRaw senha em texto puro (será criptografada)
     * @return usuário criado
     */
    @Transactional
    public Usuario criarUsuario(Usuario usuario, String senhaRaw) {
        // Verificar se e-mail já existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            log.warn("Tentativa de cadastro com e-mail já existente: {}", usuario.getEmail());
            throw new BusinessException("E-mail já cadastrado");
        }

        // Criptografar a senha
        String senhaCriptografada = passwordEncoder.encode(senhaRaw);
        usuario.setSenha(senhaCriptografada);

        // Salvar usuário
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso: ID={}, Email={}", usuarioSalvo.getId(), usuarioSalvo.getEmail());

        return usuarioSalvo;
    }

    /**
     * Busca usuário por ID
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Busca usuário por e-mail
     */
    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com e-mail: " + email));
    }

    /**
     * Lista todos os usuários ativos
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos(UUID usuarioId) {
        return usuarioRepository.findAllAtivos(usuarioId);
    }

    /**
     * Atualiza dados do usuário
     */
    @Transactional
    public Usuario atualizarUsuario(UUID id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(usuarioAtualizado.getNome());
        usuario.setTelefone(usuarioAtualizado.getTelefone());
        usuario.setCidade(usuarioAtualizado.getCidade());
        usuario.setEstado(usuarioAtualizado.getEstado());
        usuario.setLatitude(usuarioAtualizado.getLatitude());
        usuario.setLongitude(usuarioAtualizado.getLongitude());

        return usuarioRepository.save(usuario);
    }

    /**
     * Desativa usuário (soft delete)
     */
    @Transactional
    public void deletarUsuario(UUID id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuário desativado: ID={}", id);
    }
}
