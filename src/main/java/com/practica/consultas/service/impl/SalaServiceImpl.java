package com.practica.consultas.service.impl;

import com.practica.consultas.model.Sala;
import com.practica.consultas.repository.SalaRepository;
import com.practica.consultas.service.ISalaService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luis
 */
@Service
@Transactional
@RequiredArgsConstructor
public class SalaServiceImpl implements ISalaService {

    private final SalaRepository salaRepository;

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
