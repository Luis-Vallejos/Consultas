package com.practica.consultas.service.impl;

import com.practica.consultas.request.SalaRequest;
import com.practica.consultas.model.Equipo;
import com.practica.consultas.model.Sala;
import com.practica.consultas.repository.EquipoRepository;
import com.practica.consultas.repository.SalaRepository;
import com.practica.consultas.repository.specification.SalaSpecification;
import com.practica.consultas.service.ISalaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class SalaServiceImpl implements ISalaService {

    private final SalaRepository salaRepository;
    private final EquipoRepository equipoRepository;

    @Override
    // Actualizamos la firma y la lógica para usar la nueva especificación
    public Page<Sala> buscar(Integer capacidadMinima, String tipoEquipo, Boolean activa, Pageable pageable) {
        Specification<Sala> spec = Specification.where(SalaSpecification.tieneCapacidadMinima(capacidadMinima))
                .and(SalaSpecification.conTipoDeEquipo(tipoEquipo)) // Usamos el nuevo método
                .and(SalaSpecification.estaActiva(activa));
        return salaRepository.findAll(spec, pageable);
    }

    @Override
    public List<Sala> findSalasDisponibles(LocalDateTime inicio, LocalDateTime fin, Integer capacidad, List<Long> equipoIds, Boolean activa) {
        return salaRepository.findSalasDisponibles(inicio, fin, capacidad, equipoIds, activa);
    }

    @Override
    public Sala crear(SalaRequest salaRequest) {
        Sala sala = new Sala();
        sala.setNombre(salaRequest.nombre());
        sala.setCapacidad(salaRequest.capacidad());
        sala.setUbicacion(salaRequest.ubicacion());
        sala.setActiva(salaRequest.activa());

        if (salaRequest.equipoIds() != null && !salaRequest.equipoIds().isEmpty()) {
            Set<Equipo> equipos = new HashSet<>(equipoRepository.findAllById(salaRequest.equipoIds()));
            sala.setEquipos(equipos);
        }

        return salaRepository.save(sala);
    }

    @Override
    public Sala actualizar(Long id, SalaRequest salaRequest) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada con id: " + id));

        sala.setNombre(salaRequest.nombre());
        sala.setCapacidad(salaRequest.capacidad());
        sala.setUbicacion(salaRequest.ubicacion());
        sala.setActiva(salaRequest.activa());

        if (salaRequest.equipoIds() != null) {
            Set<Equipo> equipos = new HashSet<>(equipoRepository.findAllById(salaRequest.equipoIds()));
            sala.setEquipos(equipos);
        }

        return salaRepository.save(sala);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return salaRepository.existsByNombre(nombre);
    }

    @Override
    public Sala findById(Long id) {
        return salaRepository.findById(id).orElse(null);
    }

    @Override
    public ArrayList<Sala> findAll() {
        return (ArrayList<Sala>) salaRepository.findAll();
    }

    @Override
    public Sala create(Sala entity) {
        return salaRepository.save(entity);
    }

    @Override
    public Sala edit(Long id, Sala updated) {
        Sala sala = findById(id);
        if (sala == null) {
            return null;
        }
        sala.setNombre(updated.getNombre());
        sala.setCapacidad(updated.getCapacidad());
        return salaRepository.save(sala);
    }

    @Override
    public boolean delete(Long id) {
        if (!salaRepository.existsById(id)) {
            return false;
        }
        salaRepository.deleteById(id);
        return true;
    }
}
