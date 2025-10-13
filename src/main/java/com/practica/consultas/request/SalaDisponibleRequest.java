package com.practica.consultas.request;

import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

public record SalaDisponibleRequest(
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime inicio,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime fin,
        Integer capacidad,
        Set<Long> equipoIds,
        Boolean activa) {

}
