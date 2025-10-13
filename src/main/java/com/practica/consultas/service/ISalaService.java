package com.practica.consultas.service;

import com.practica.consultas.request.SalaRequest;
import com.practica.consultas.model.Sala;
import com.practica.consultas.service.generic.ICrudService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISalaService extends ICrudService<Sala, Long> {

    boolean existsByNombre(String nombre);

    Sala crear(SalaRequest salaRequest);

    Sala actualizar(Long id, SalaRequest salaRequest);

    Page<Sala> buscar(Integer capacidadMinima, String tipoEquipo, Boolean activa, Pageable pageable);

    List<Sala> findSalasDisponibles(LocalDateTime inicio, LocalDateTime fin, Integer capacidad, List<Long> equipoIds, Boolean activa);

}
