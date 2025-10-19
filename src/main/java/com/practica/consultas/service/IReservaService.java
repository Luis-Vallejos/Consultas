package com.practica.consultas.service;

import com.practica.consultas.model.Reserva;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.generic.ICrudService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface IReservaService extends ICrudService<Reserva, Long> {

    boolean existeSolape(Long salaId, LocalDateTime inicio, LocalDateTime fin);

    Reserva crearReserva(ReservaRequest request, Authentication authentication);

    Reserva cancelarReserva(Long reservaId, Authentication authentication);

    Reserva obtenerReservaPorId(Long reservaId, Authentication authentication);

    List<Reserva> obtenerReservasPorUsuario(Authentication authentication);

}
