package com.practica.consultas.dto;

import com.practica.consultas.model.Equipo;

public record EquipoDto(
        Long id,
        String nombre,
        String tipo,
        String estado,
        String descripcion) {

    public static EquipoDto fromEntity(Equipo equipo) {
        return new EquipoDto(
                equipo.getId(),
                equipo.getNombre(),
                equipo.getTipo(),
                equipo.getEstado(),
                equipo.getDescripcion());
    }
}
