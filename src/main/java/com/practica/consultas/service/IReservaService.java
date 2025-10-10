package com.practica.consultas.service;

import com.practica.consultas.model.Reserva;
import com.practica.consultas.service.generic.ICrudService;
import java.time.LocalDateTime;

/**
 *
 * @author Luis
 */
public interface IReservaService extends ICrudService<Reserva, Long> {

    boolean existeSolape(Long salaId, LocalDateTime inicio, LocalDateTime fin);
}
