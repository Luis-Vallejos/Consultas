package com.practica.consultas.controller;

import com.practica.consultas.dto.EquipoDto;
import com.practica.consultas.request.EquipoRequest;
import com.practica.consultas.service.IEquipoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    @Autowired
    private IEquipoService equipoService;

    // Endpoint público para listar todos los equipos
    @GetMapping
    public ResponseEntity<List<EquipoDto>> listAll() {
        return ResponseEntity.ok(equipoService.findAllDtos());
    }

    // Endpoint público para obtener un equipo por ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDto> getById(@PathVariable Long id) {
        EquipoDto e = equipoService.findDtoById(id);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }

    // Endpoint solo para ADMIN para crear un nuevo equipo
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipoDto> create(@RequestBody EquipoRequest request) {
        EquipoDto nuevo = equipoService.create(request);
        return ResponseEntity.ok(nuevo);
    }

    // Endpoint solo para ADMIN para actualizar un equipo existente
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EquipoDto> update(@PathVariable Long id, @RequestBody EquipoRequest request) {
        EquipoDto actualizado = equipoService.edit(id, request);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    // Endpoint solo para ADMIN para eliminar un equipo
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = equipoService.delete(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Endpoint público para listar equipos por su estado (ej. "DISPONIBLE")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<EquipoDto>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoService.findByEstado(estado));
    }
}
