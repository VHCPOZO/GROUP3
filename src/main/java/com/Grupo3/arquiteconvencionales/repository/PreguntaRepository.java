package com.Grupo3.arquiteconvencionales.repository;

import com.Grupo3.arquiteconvencionales.model.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long> {
    List<Pregunta> findByTema(String tema);
    List<Pregunta> findAllByOrderByIdAsc();
}