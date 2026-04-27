package com.Grupo3.arquiteconvencionales.repository;

import com.Grupo3.arquiteconvencionales.model.Resultado;
import com.Grupo3.arquiteconvencionales.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultadoRepository extends JpaRepository<Resultado, Long> {
    List<Resultado> findByUsuarioOrderByFechaRealizacionDesc(Usuario usuario);
    List<Resultado> findByUsuarioUsername(String username);
    List<Resultado> findAllByOrderByFechaRealizacionDesc();
}