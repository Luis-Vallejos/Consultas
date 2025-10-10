package com.practica.consultas.service;

import com.practica.consultas.model.Equipo;
import com.practica.consultas.service.generic.ICrudService;
import java.util.List;

/**
 *
 * @author Luis
 */
public interface IEquipoService extends ICrudService<Equipo, Long> {

    List<Equipo> findByEstado(String estado);
}
