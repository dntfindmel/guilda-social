package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Grupo;
import com.guildasocial.domain.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {

    List<Mensagem> findByGrupoOrderByDataEnvioAsc(Grupo grupo);

    @Query("SELECT m FROM Mensagem m WHERE m.grupo.id = :grupoId ORDER BY m.dataEnvio ASC")
    List<Mensagem> findByGrupoId(@Param("grupoId") UUID grupoId);
}
