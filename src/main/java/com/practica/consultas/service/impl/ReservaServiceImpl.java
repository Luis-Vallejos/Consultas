package com.practica.consultas.service.impl;

import com.practica.consultas.model.Reserva;
import com.practica.consultas.repository.ReservaRepository;
import com.practica.consultas.service.IReservaService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
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
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;

    @Override
    public boolean existeSolape(Long salaId, LocalDateTime inicio, LocalDateTime fin) {
        return reservaRepository.existsBySalaAndFechas(salaId, inicio, fin);
    }

    @Override
    public Reserva findById(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }

    @Override
    public ArrayList<Reserva> findAll() {
        return (ArrayList<Reserva>) reservaRepository.findAll();
    }

    @Override
    public Reserva create(Reserva entity) {
        return reservaRepository.save(entity);
    }

    @Override
    public Reserva edit(Long id, Reserva updated) {
        Reserva reserva = findById(id);
        if (reserva == null) {
            return null;
        }
        reserva.setInicio(updated.getInicio());
        reserva.setFin(updated.getFin());
        reserva.setSala(updated.getSala());
        reserva.setUsuario(updated.getUsuario());
        return reservaRepository.save(reserva);
    }

    @Override
    public boolean delete(Long id) {
        if (!reservaRepository.existsById(id)) {
            return false;
        }
        reservaRepository.deleteById(id);
        return true;
    }
}
