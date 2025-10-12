package com.practica.consultas.service;

import com.practica.consultas.dto.EquipoDto;
import com.practica.consultas.model.Equipo;
import com.practica.consultas.request.EquipoRequest;
import com.practica.consultas.service.generic.ICrudService;
import java.util.List;

public interface IEquipoService extends ICrudService<Equipo, Long> {

    // Métodos que devuelven DTOs para ser usados por el controlador
    List<EquipoDto> findByEstado(String estado);

    List<EquipoDto> findAllDtos();

    EquipoDto findDtoById(Long id);

    EquipoDto create(EquipoRequest request);

    EquipoDto edit(Long id, EquipoRequest request);
}
