package com.practica.consultas.request;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;

public record ReservaRequest(
        @NotNull(message = "El ID de la sala no puede ser nulo.")
        Long salaId,
        
        @NotNull(message = "La fecha de inicio no puede ser nula.")
        @Future(message = "La fecha de inicio debe ser en el futuro.")
        LocalDateTime inicio,
        
        @NotNull(message = "La fecha de fin no puede ser nula.")
        @Future(message = "La fecha de fin debe ser en el futuro.")
        LocalDateTime fin) {

}
