package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Grupo;
import com.guildasocial.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface GrupoRepository extends JpaRepository<Grupo, UUID> {

    List<Grupo> findByAtivoTrue();

    List<Grupo> findByLider(Usuario lider);

    @Query("SELECT g FROM Grupo g WHERE :usuario MEMBER OF g.membros")
    List<Grupo> findGruposByMembro(@Param("usuario") Usuario usuario);

    @Query("SELECT g FROM Grupo g WHERE g.jogo.id = :jogoId AND g.ativo = true")
    List<Grupo> findByJogo(@Param("jogoId") UUID jogoId);
}
