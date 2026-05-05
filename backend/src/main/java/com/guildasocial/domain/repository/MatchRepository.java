package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findByUsuario1AndStatus(Usuario usuario1, String status);

    List<Match> findByUsuario2AndStatus(Usuario usuario2, String status);

    @Query("SELECT m FROM Match m WHERE (m.usuario1 = :usuario OR m.usuario2 = :usuario) AND m.status = :status")
    List<Match> findMatchesByUsuario(@Param("usuario") Usuario usuario, @Param("status") String status);

    @Query("SELECT m FROM Match m WHERE (m.usuario1.id = :usuarioId OR m.usuario2.id = :usuarioId)")
    @NonNull
    List<Match> findAllByUsuarioId(@Param("usuarioId") @NonNull UUID usuarioId);

    @Query("SELECT m FROM Match m WHERE (m.usuario1.id = :usuarioId OR m.usuario2.id = :usuarioId) AND m.status = 'ACEITO'")
    @NonNull
    List<Match> findAcceptedByUsuarioId(@Param("usuarioId") @NonNull UUID usuarioId);

    boolean existsByUsuario1AndUsuario2(Usuario usuario1, Usuario usuario2);

    Optional<Match> findByUsuario1AndUsuario2(Usuario usuario1, Usuario usuario2);
}
