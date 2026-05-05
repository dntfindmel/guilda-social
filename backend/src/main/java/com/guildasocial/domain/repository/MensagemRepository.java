package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Match;
import com.guildasocial.domain.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface MensagemRepository extends JpaRepository<Mensagem, UUID> {

    List<Mensagem> findByMatchOrderByDataEnvioAsc(Match match);

    @Query("SELECT m FROM Mensagem m WHERE m.match.id = :matchId ORDER BY m.dataEnvio ASC")
    List<Mensagem> findByMatchIdOrderByDataEnvioAsc(@Param("matchId") UUID matchId);

    Mensagem findTopByMatchOrderByDataEnvioDesc(Match match);
}
