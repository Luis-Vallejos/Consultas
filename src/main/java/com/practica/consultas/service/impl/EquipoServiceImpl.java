package com.practica.consultas.service.impl;

import com.practica.consultas.dto.EquipoDto;
import com.practica.consultas.exceptions.ResourceNotFoundException;
import com.practica.consultas.model.Equipo;
import com.practica.consultas.repository.EquipoRepository;
import com.practica.consultas.request.EquipoRequest;
import com.practica.consultas.service.IEquipoService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipoServiceImpl implements IEquipoService {

    private final EquipoRepository equipoRepository;

    @Override
    public List<EquipoDto> findByEstado(String estado) {
        return equipoRepository.findByEstado(estado).stream()
                .map(EquipoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public EquipoDto findDtoById(Long id) {
        return equipoRepository.findById(id)
                .map(EquipoDto::fromEntity)
                .orElse(null);
    }

    @Override
    public Equipo findById(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }

    @Override
    public List<EquipoDto> findAllDtos() {
        return equipoRepository.findAll().stream()
                .map(EquipoDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Equipo> findAll() {
        return (ArrayList<Equipo>) equipoRepository.findAll();
    }

    @Override
    public EquipoDto create(EquipoRequest request) {
        Equipo equipo = new Equipo();
        equipo.setNombre(request.nombre());
        equipo.setTipo(request.tipo());
        equipo.setEstado(request.estado());
        equipo.setDescripcion(request.descripcion());

        Equipo guardado = equipoRepository.save(equipo);
        return EquipoDto.fromEntity(guardado);
    }

    @Override
    public Equipo create(Equipo entity) {
        return equipoRepository.save(entity);
    }

    @Override
    public EquipoDto edit(Long id, EquipoRequest request) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el equipo con id: " + id));

        equipo.setNombre(request.nombre());
        equipo.setEstado(request.estado());
        equipo.setTipo(request.tipo());
        equipo.setDescripcion(request.descripcion());

        Equipo actualizado = equipoRepository.save(equipo);
        return EquipoDto.fromEntity(actualizado);
    }

    @Override
    public Equipo edit(Long id, Equipo updated) {
        Equipo equipo = findById(id);
        if (equipo == null) {
            return null;
        }
        equipo.setNombre(updated.getNombre());
        equipo.setEstado(updated.getEstado());
        equipo.setDescripcion(updated.getDescripcion());
        return equipoRepository.save(equipo);
    }

    @Override
    public boolean delete(Long id) {
        if (!equipoRepository.existsById(id)) {
            return false;
        }
        equipoRepository.deleteById(id);
        return true;
    }
}
