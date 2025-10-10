package com.practica.consultas.service.impl;

import com.practica.consultas.model.Equipo;
import com.practica.consultas.repository.EquipoRepository;
import com.practica.consultas.service.IEquipoService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luis
 */
@Service
@Transactional
@RequiredArgsConstructor
public class EquipoServiceImpl implements IEquipoService {

    private final EquipoRepository equipoRepository;

    @Override
    public List<Equipo> findByEstado(String estado) {
        return equipoRepository.findByEstado(estado);
    }

    @Override
    public Equipo findById(Long id) {
        return equipoRepository.findById(id).orElse(null);
    }

    @Override
    public ArrayList<Equipo> findAll() {
        return (ArrayList<Equipo>) equipoRepository.findAll();
    }

    @Override
    public Equipo create(Equipo entity) {
        return equipoRepository.save(entity);
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
