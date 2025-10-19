package com.practica.consultas.controller;

import com.practica.consultas.dto.EquipoDto;
import com.practica.consultas.request.EquipoRequest;
import com.practica.consultas.service.IEquipoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EquipoController {

    private final IEquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<EquipoDto>> listAll() {
        return ResponseEntity.ok(equipoService.findAllDtos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> getById(@PathVariable Long id) {
        EquipoDto e = equipoService.findDtoById(id);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipoDto> create(@Valid @RequestBody EquipoRequest request) {
        EquipoDto nuevo = equipoService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevo.id())
                .toUri();

        return ResponseEntity.created(location).body(nuevo);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipoDto> update(@PathVariable Long id, @Valid @RequestBody EquipoRequest request) {
        EquipoDto actualizado = equipoService.edit(id, request);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = equipoService.delete(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EquipoDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoService.findByEstado(estado));
    }
}
