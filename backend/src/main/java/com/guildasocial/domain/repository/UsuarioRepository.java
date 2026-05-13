package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByResetToken(String resetToken);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND u.id != :usuarioId")
    List<Usuario> findAllAtivos(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true")
    List<Usuario> findAllAtivos();
}
