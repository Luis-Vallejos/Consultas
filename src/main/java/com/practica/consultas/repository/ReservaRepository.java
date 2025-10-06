package com.practica.consultas.repository;

import com.practica.consultas.model.Reserva;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Luis
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    boolean existsBySalaIdAndInicioBeforeAndFinAfter(
            Long salaId,
            LocalDateTime fin,
            LocalDateTime inicio
    );
}
