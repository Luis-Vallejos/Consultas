package com.practica.consultas.dto;

import com.practica.consultas.model.Reserva;
import java.time.LocalDateTime;

/**
 * DTO para formatear la respuesta al crear o solicitar una reserva. Muestra
 * información relevante de la reserva y sus entidades asociadas.
 */
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
