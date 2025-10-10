package com.practica.consultas.controller;

import com.practica.consultas.controller.generic.CrudController;
import com.practica.consultas.model.Reserva;
import com.practica.consultas.service.IReservaService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController implements CrudController<Reserva, Long> {

    @Autowired
    private IReservaService reservaService;

    @Override
    public ResponseEntity<List<Reserva>> listAll() {
        return ResponseEntity.ok(reservaService.findAll());
    }

    @Override
    public ResponseEntity<Reserva> getById(@PathVariable Long id) {
        Reserva reserva = reservaService.findById(id);
        return reserva != null ? ResponseEntity.ok(reserva) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Reserva> create(@RequestBody Reserva dto) {
        Reserva nueva = reservaService.create(dto);
        return ResponseEntity.ok(nueva);
    }

    @Override
    public ResponseEntity<Reserva> update(@PathVariable Long id, @RequestBody Reserva dto) {
        dto.setId(id);
        Reserva actualizada = reservaService.edit(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
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
