package com.practica.consultas.repository;

import com.practica.consultas.model.Sala;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Luis
 */
public interface SalaRepository extends JpaRepository<Sala, Long>, JpaSpecificationExecutor<Sala> {

    boolean existsByNombre(String nombre);

    @Query("SELECT s FROM Sala s WHERE "
            + "(:capacidad IS NULL OR s.capacidad >= :capacidad) AND "
            + "(:activa IS NULL OR s.activa = :activa) AND "
            + "s.id NOT IN ("
            + "  SELECT r.sala.id FROM Reserva r WHERE "
            + "  r.estado = 'CONFIRMADA' AND "
            + "  (r.inicio < :fin AND r.fin > :inicio)"
            + ") AND "
            + "(:equipoIds IS NULL OR ("
            + "  SELECT COUNT(e.id) FROM s.equipos e WHERE e.id IN :equipoIds"
            + ") = :#{#equipoIds == null ? 0 : #equipoIds.size()})"
    )
    List<Sala> findSalasDisponibles(@Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("capacidad") Integer capacidad,
            @Param("equipoIds") List<Long> equipoIds,
            @Param("activa") Boolean activa);

}
