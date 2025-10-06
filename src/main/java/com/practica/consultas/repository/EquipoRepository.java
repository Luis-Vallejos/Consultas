package com.practica.consultas.repository;

import com.practica.consultas.model.Equipo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Luis
 */
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    List<Equipo> findByEstado(String estado);
}
