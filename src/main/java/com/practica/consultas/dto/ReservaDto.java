package com.practica.consultas.dto;

import com.practica.consultas.model.Reserva;
import java.time.LocalDateTime;

public record ReservaDto(
        Long id,
        LocalDateTime inicio,
        LocalDateTime fin,
        String estado,
        Long usuarioId,
        String nombreUsuario,
        Long salaId,
        String nombreSala) {

    public static ReservaDto fromEntity(Reserva reserva) {
        return new ReservaDto(
                reserva.getId(),
                reserva.getInicio(),
                reserva.getFin(),
                reserva.getEstado(),
                reserva.getUsuario().getId(),
                reserva.getUsuario().getNombre(),
                reserva.getSala().getId(),
                reserva.getSala().getNombre()
        );
    }
}
