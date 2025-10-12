package com.practica.consultas.request;

import java.time.LocalDateTime;

/**
 * DTO para encapsular los datos necesarios para crear una nueva reserva. Se
 * utiliza en el controlador para recibir la información del cliente.
 */
public record ReservaRequest(
        Long salaId,
        LocalDateTime inicio,
        LocalDateTime fin) {

}
