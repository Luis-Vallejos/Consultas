package com.practica.consultas.request;

import java.util.Set;

public record SalaRequest(
        String nombre,
        Integer capacidad,
        String ubicacion,
        boolean activa,
        Set<Long> equipoIds) {

}
