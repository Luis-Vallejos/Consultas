package com.practica.consultas.controller;

import com.practica.consultas.controller.generic.CrudController;
import com.practica.consultas.model.Sala;
import com.practica.consultas.service.ISalaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/salas")
@CrossOrigin(origins = "*")
public class SalaController implements CrudController<Sala, Long> {

    @Autowired
    private ISalaService salaService;

    @Override
    public ResponseEntity<List<Sala>> listAll() {
        return ResponseEntity.ok(salaService.findAll());
    }

    @Override
    public ResponseEntity<Sala> getById(@PathVariable Long id) {
        Sala s = salaService.findById(id);
        return s != null ? ResponseEntity.ok(s) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Sala> create(@RequestBody Sala dto) {
        Sala nueva = salaService.create(dto);
        return ResponseEntity.ok(nueva);
    }

    @Override
    public ResponseEntity<Sala> update(@PathVariable Long id, @RequestBody Sala dto) {
        Sala actualizada = salaService.edit(id, dto);
        return actualizada != null ? ResponseEntity.ok(actualizada) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean eliminado = salaService.delete(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Extra: verificar si existe sala por nombre
    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(salaService.existsByNombre(nombre));
    }
}
