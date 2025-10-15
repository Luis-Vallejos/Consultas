package com.practica.consultas.service.impl;

import com.practica.consultas.exceptions.ReglaNegocioException;
import com.practica.consultas.exceptions.ResourceNotFoundException;
import com.practica.consultas.model.Reserva;
import com.practica.consultas.model.Sala;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.ReservaRepository;
import com.practica.consultas.repository.SalaRepository;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.IReservaService;
import com.practica.consultas.service.ISalaLockService;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException; // <-- IMPORT CORREGIDO
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservaServiceImpl implements IReservaService {

    private final ReservaRepository reservaRepository;
    private final SalaRepository salaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ISalaLockService salaLockService;

    @Override
    public Reserva crearReserva(ReservaRequest request) {
        return salaLockService.runWithLock(request.salaId(), () -> {
            try {
                if (request.inicio().isAfter(request.fin()) || request.inicio().isEqual(request.fin())) {
                    throw new ReglaNegocioException("La hora de fin debe ser posterior a la hora de inicio.");
                }
                if (Duration.between(request.inicio(), request.fin()).toMinutes() < 30) {
                    throw new ReglaNegocioException("La duración mínima de la reserva es de 30 minutos.");
                }

                if (existeSolape(request.salaId(), request.inicio(), request.fin())) {
                    throw new ReglaNegocioException("La sala ya está ocupada en el horario seleccionado.");
                }

                Sala sala = salaRepository.findById(request.salaId())
                        .orElseThrow(() -> new ResourceNotFoundException("No se encontró la sala con id: " + request.salaId()));
                String correoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
                Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado."));

                Reserva nuevaReserva = Reserva.builder()
                        .sala(sala)
                        .usuario(usuario)
                        .inicio(request.inicio())
                        .fin(request.fin())
                        .estado("CONFIRMADA")
                        .build();

                return reservaRepository.save(nuevaReserva);

            } catch (ObjectOptimisticLockingFailureException ex) {
                throw new ReglaNegocioException("Conflicto de concurrencia. La sala fue reservada por otro usuario. Por favor, intente de nuevo.");
            }
        });
    }

    // ... El resto del archivo no necesita cambios ...
    @Override
    public Reserva cancelarReserva(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con id: " + reservaId));

        if (reserva.getEstado().equals("CANCELADA")) {
            throw new ReglaNegocioException("Esta reserva ya ha sido cancelada.");
        }

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        Usuario usuarioActual = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario actual no encontrado."));

        if (!isAdmin && !reserva.getUsuario().getId().equals(usuarioActual.getId())) {
            throw new ReglaNegocioException("No tiene permisos para cancelar esta reserva.");
        }
        if (!isAdmin && LocalDateTime.now().isAfter(reserva.getInicio().minusHours(1))) {
            throw new ReglaNegocioException("Solo puede cancelar la reserva hasta 1 hora antes de su inicio.");
        }

        reserva.setEstado("CANCELADA");
        return reservaRepository.save(reserva);
    }

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
        reserva.setEstado(updated.getEstado());
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
