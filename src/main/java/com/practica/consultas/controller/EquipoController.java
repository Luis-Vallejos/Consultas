package com.practica.consultas.controller;

import com.practica.consultas.controller.generic.CrudController;
import com.practica.consultas.model.Equipo;
import com.practica.consultas.service.IEquipoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController implements CrudController<Equipo, Long> {

    @Autowired
    private IEquipoService equipoService;

    @Override
    public ResponseEntity<List<Equipo>> listAll() {
        return ResponseEntity.ok(equipoService.findAll());
    }

    @Override
    public ResponseEntity<Equipo> getById(@PathVariable Long id) {
        Equipo e = equipoService.findById(id);
        return e != null ? ResponseEntity.ok(e) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Equipo> create(@RequestBody Equipo dto) {
        Equipo nuevo = equipoService.create(dto);
        return ResponseEntity.ok(nuevo);
    }

    @Override
    public ResponseEntity<Equipo> update(@PathVariable Long id, @RequestBody Equipo dto) {
        Equipo actualizado = equipoService.edit(id, dto);
        return actualizado != null ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = equipoService.delete(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Extra: listar por estado (DISPONIBLE, MANTENIMIENTO, etc.)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Equipo>> findByEstado(@PathVariable String estado) {
        return ResponseEntity.ok(equipoService.findByEstado(estado));
    }
}
