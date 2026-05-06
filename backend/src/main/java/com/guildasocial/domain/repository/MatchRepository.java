package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<Match, UUID> {

    List<Match> findByUsuario1AndStatus(Usuario usuario1, String status);

    List<Match> findByUsuario2AndStatus(Usuario usuario2, String status);

    @Query("SELECT m FROM Match m WHERE (m.usuario1 = :usuario OR m.usuario2 = :usuario) AND m.status = :status")
    List<Match> findMatchesByUsuario(@Param("usuario") Usuario usuario, @Param("status") String status);

    @Query("SELECT m FROM Match m WHERE (m.usuario1.id = :usuarioId OR m.usuario2.id = :usuarioId)")
    List<Match> findAllByUsuarioId(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT m FROM Match m WHERE (m.usuario1.id = :usuarioId AND m.usuario2.id = :alvoId) " +
           "OR (m.usuario1.id = :alvoId AND m.usuario2.id = :usuarioId)")
    Optional<Match> findMatchBetweenUsers(@Param("usuarioId") UUID usuarioId,
                                          @Param("alvoId") UUID alvoId);

    boolean existsByUsuario1AndUsuario2(Usuario usuario1, Usuario usuario2);

    // Verificar se o usuário atual já solicitou (PENDENTE enviado por ele)
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m " +
           "WHERE m.usuario1.id = :usuarioId AND m.usuario2.id = :alvoId AND m.status = 'PENDENTE'")
    boolean existeSolicitacaoEnviada(@Param("usuarioId") UUID usuarioId,
                                      @Param("alvoId") UUID alvoId);

    // Verificar se o usuário atual já passou
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m " +
           "WHERE m.usuario1.id = :usuarioId AND m.usuario2.id = :alvoId AND m.status = 'PASSADO'")
    boolean existePassado(@Param("usuarioId") UUID usuarioId,
                          @Param("alvoId") UUID alvoId);

    // Verificar se já existe match ACEITO (amigos)
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Match m " +
           "WHERE ((m.usuario1.id = :usuarioId AND m.usuario2.id = :alvoId) OR " +
           "(m.usuario1.id = :alvoId AND m.usuario2.id = :usuarioId)) " +
           "AND m.status = 'ACEITO'")
    boolean existeMatchAceito(@Param("usuarioId") UUID usuarioId,
                               @Param("alvoId") UUID alvoId);
}
