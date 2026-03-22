package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true AND u.id != :usuarioId")
    List<Usuario> findAllAtivos(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT u FROM Usuario u WHERE u.cidade = :cidade AND u.estado = :estado AND u.ativo = true")
    List<Usuario> findByLocalizacao(@Param("cidade") String cidade, @Param("estado") String estado);

    @Query("SELECT u FROM Usuario u WHERE u.ativo = true")
    List<Usuario> findAllAtivos();
}
