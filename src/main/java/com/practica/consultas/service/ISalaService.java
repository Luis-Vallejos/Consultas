package com.practica.consultas.service;

import com.practica.consultas.model.Sala;
import com.practica.consultas.service.generic.ICrudService;

/**
 *
 * @author Luis
 */
public interface ISalaService extends ICrudService<Sala, Long> {

    boolean existsByNombre(String nombre);
}
