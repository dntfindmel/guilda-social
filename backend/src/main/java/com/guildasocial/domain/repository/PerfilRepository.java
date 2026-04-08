package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.PerfilJogador;
import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PerfilRepository extends JpaRepository<PerfilJogador, UUID> {

    Optional<PerfilJogador> findByUsuario(Usuario usuario);

    @Query("SELECT p FROM PerfilJogador p WHERE p.usuario.ativo = true AND p.usuario.id != :usuarioId")
    List<PerfilJogador> findAllExcept(@Param("usuarioId") UUID usuarioId);
}
