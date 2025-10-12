package com.practica.consultas.request;

import java.time.LocalDateTime;

public record ReservaRequest(
        Long salaId,
        LocalDateTime inicio,
        LocalDateTime fin) {

}
