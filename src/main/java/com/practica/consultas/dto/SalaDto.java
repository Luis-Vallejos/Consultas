package com.practica.consultas.dto;

import com.practica.consultas.model.Sala;
import java.util.Set;
import java.util.stream.Collectors;

public record SalaDto(
        Long id,
        String nombre,
        Integer capacidad,
        String ubicacion,
        boolean activa,
        Set<EquipoDto> equipos) {

    public static SalaDto fromEntity(Sala sala) {
        return new SalaDto(
                sala.getId(),
                sala.getNombre(),
                sala.getCapacidad(),
                sala.getUbicacion(),
                sala.isActiva(),
                sala.getEquipos().stream()
                        .map(EquipoDto::fromEntity)
                        .collect(Collectors.toSet()));
    }
}
