package com.practica.consultas.controller;

import com.practica.consultas.dto.SalaDto;
import com.practica.consultas.request.SalaRequest;
import com.practica.consultas.model.Sala;
import com.practica.consultas.service.ISalaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salas")
@CrossOrigin(origins = "*")
public class SalaController {

    @Autowired
    private ISalaService salaService;

    @GetMapping
    public ResponseEntity<Page<SalaDto>> buscarSalas(
            @RequestParam(required = false) Integer capacidadMinima,
            @RequestParam(required = false) Long equipoId,
            @RequestParam(required = false) Boolean activa,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable
    ) {
        Page<Sala> salas = salaService.buscar(capacidadMinima, equipoId, activa, pageable);
        Page<SalaDto> dtos = salas.map(SalaDto::fromEntity);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaDto> getById(@PathVariable Long id) {
        Sala s = salaService.findById(id);
        return s != null ? ResponseEntity.ok(SalaDto.fromEntity(s)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaDto> create(@RequestBody SalaRequest request) {
        Sala nueva = salaService.crear(request);
        return ResponseEntity.ok(SalaDto.fromEntity(nueva));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SalaDto> update(@PathVariable Long id, @RequestBody SalaRequest request) {
        Sala actualizada = salaService.actualizar(id, request);
        return actualizada != null ? ResponseEntity.ok(SalaDto.fromEntity(actualizada)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = salaService.delete(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(salaService.existsByNombre(nombre));
    }
}
