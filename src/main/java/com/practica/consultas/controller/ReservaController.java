package com.practica.consultas.controller;

import com.practica.consultas.dto.ReservaDto;
import com.practica.consultas.model.Reserva;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.IReservaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ReservaController {

    private final IReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDto> crearNuevaReserva(
            @Valid @RequestBody ReservaRequest request,
            Authentication authentication
    ) {
        Reserva nuevaReserva = reservaService.crearReserva(request, authentication);
        ReservaDto dto = ReservaDto.fromEntity(nuevaReserva);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaReserva.getId())
                .toUri();

        return ResponseEntity.created(location).body(dto);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<ReservaDto> cancelarUnaReserva(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id, authentication);
        return ResponseEntity.ok(ReservaDto.fromEntity(reservaCancelada));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listarReservas(Authentication authentication) {
        List<ReservaDto> reservas = reservaService.obtenerReservasPorUsuario(authentication).stream()
                .map(ReservaDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> obtenerReservaPorId(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Reserva reserva = reservaService.obtenerReservaPorId(id, authentication);
        return ResponseEntity.ok(ReservaDto.fromEntity(reserva));
    }
}
