package com.practica.consultas.repository;

import com.practica.consultas.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Luis
 */
public interface SalaRepository extends JpaRepository<Sala, Long> {

    boolean existsByNombre(String nombre);
}
