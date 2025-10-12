package com.practica.consultas.service;

import com.practica.consultas.model.Reserva;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.generic.ICrudService;
import java.time.LocalDateTime;

public interface IReservaService extends ICrudService<Reserva, Long> {

    boolean existeSolape(Long salaId, LocalDateTime inicio, LocalDateTime fin);

    Reserva crearReserva(ReservaRequest request);

    Reserva cancelarReserva(Long reservaId);
}
