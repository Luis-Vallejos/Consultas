package com.practica.consultas.service;

import com.practica.consultas.request.SalaRequest;
import com.practica.consultas.model.Sala;
import com.practica.consultas.service.generic.ICrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Luis
 */
public interface ISalaService extends ICrudService<Sala, Long> {

    boolean existsByNombre(String nombre);

    Sala crear(SalaRequest salaRequest);

    Sala actualizar(Long id, SalaRequest salaRequest);

    Page<Sala> buscar(Integer capacidadMinima, Long equipoId, Boolean activa, Pageable pageable);

}
