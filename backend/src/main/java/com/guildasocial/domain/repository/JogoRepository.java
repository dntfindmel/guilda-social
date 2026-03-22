package com.guildasocial.domain.repository;

import com.guildasocial.domain.model.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JogoRepository extends JpaRepository<Jogo, UUID> {

    List<Jogo> findByTipo(Jogo.TipoJogo tipo);

    List<Jogo> findByNomeContainingIgnoreCase(String nome);
}
