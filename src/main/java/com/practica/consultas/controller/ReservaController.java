package com.practica.consultas.controller;

import com.practica.consultas.dto.ReservaDto;
import com.practica.consultas.model.Reserva;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.IReservaService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private IReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaDto> crearNuevaReserva(@RequestBody ReservaRequest request) {
        Reserva nuevaReserva = reservaService.crearReserva(request);
        return new ResponseEntity<>(ReservaDto.fromEntity(nuevaReserva), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReservaDto> cancelarUnaReserva(@PathVariable Long id) {
        Reserva reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(ReservaDto.fromEntity(reservaCancelada));
    }

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listAll() {
        List<ReservaDto> reservas = reservaService.findAll().stream()
                .map(ReservaDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> getById(@PathVariable Long id) {
        Reserva reserva = reservaService.findById(id);
        return reserva != null ? ResponseEntity.ok(ReservaDto.fromEntity(reserva)) : ResponseEntity.notFound().build();
    }

    @GetMapping("/existe-solape")
    public ResponseEntity<Boolean> existeSolape(
            @RequestParam Long salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        boolean solapa = reservaService.existeSolape(salaId, inicio, fin);
        return ResponseEntity.ok(solapa);
    }
}
