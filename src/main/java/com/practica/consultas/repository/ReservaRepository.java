package com.practica.consultas.repository;

import com.practica.consultas.model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Luis
 */
@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    @Query("""
           SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END
           FROM Reserva r
           WHERE r.sala.id = :salaId
             AND r.estado = 'CONFIRMADA'
             AND (r.inicio < :fin AND r.fin > :inicio)
           """)
    boolean existsBySalaAndFechas(
            @Param("salaId") Long salaId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin
    );

    List<Reserva> findByUsuarioId(Long usuarioId);
}
