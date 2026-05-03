package com.guildasocial.domain.service;

import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Grupo;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.GrupoRepository;
import com.guildasocial.domain.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final UsuarioRepository usuarioRepository;

    public GrupoService(GrupoRepository grupoRepository, UsuarioRepository usuarioRepository) {
        this.grupoRepository = grupoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Grupo criarGrupo(Grupo grupo, UUID liderId) {
        Usuario lider = usuarioRepository.findById(liderId)
                .orElseThrow(() -> new BusinessException("Líder não encontrado"));

        grupo.setLider(lider);
        grupo.getMembros().add(lider);
        grupo.setVagasPreenchidas(1);

        return grupoRepository.save(grupo);
    }

    @Transactional(readOnly = true)
    public List<Grupo> listarGrupos() {
        return grupoRepository.findByAtivoTrue();
    }

    @Transactional(readOnly = true)
    public Grupo buscarPorId(UUID id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Grupo não encontrado"));
    }

    @Transactional
    public Grupo entrarNoGrupo(UUID grupoId, UUID usuarioId) {
        Grupo grupo = buscarPorId(grupoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (grupo.getMembros().contains(usuario)) {
            throw new BusinessException("Você já é membro deste grupo");
        }

        if (grupo.getVagasPreenchidas() >= grupo.getVagas()) {
            throw new BusinessException("Grupo está cheio");
        }

        grupo.getMembros().add(usuario);
        grupo.setVagasPreenchidas(grupo.getVagasPreenchidas() + 1);

        return grupoRepository.save(grupo);
    }

    @Transactional
    public Grupo sairDoGrupo(UUID grupoId, UUID usuarioId) {
        Grupo grupo = buscarPorId(grupoId);
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        if (!grupo.getMembros().contains(usuario)) {
            throw new BusinessException("Você não é membro deste grupo");
        }

        grupo.getMembros().remove(usuario);
        grupo.setVagasPreenchidas(grupo.getVagasPreenchidas() - 1);

        // Se o líder sair, transfere liderança
        if (grupo.getLider().getId().equals(usuarioId) && !grupo.getMembros().isEmpty()) {
            grupo.setLider(grupo.getMembros().get(0));
        }

        return grupoRepository.save(grupo);
    }
}
