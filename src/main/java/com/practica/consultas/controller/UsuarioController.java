package com.practica.consultas.controller;

import com.practica.consultas.controller.generic.CrudController;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.service.IUsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController implements CrudController<Usuario, Long> {

    @Autowired
    private IUsuarioService usuarioService;

    @Override
    public ResponseEntity<List<Usuario>> listAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    @Override
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        return usuario != null ? ResponseEntity.ok(usuario) : ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Usuario> create(@RequestBody Usuario dto) {
        Usuario nuevo = usuarioService.create(dto);
        return ResponseEntity.ok(nuevo);
    }

    @Override
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario dto) {
        dto.setId(id);
        Usuario actualizado = usuarioService.edit(id, dto);
        return ResponseEntity.ok(actualizado);
    }

    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
