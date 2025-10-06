package com.practica.consultas.controller.generic;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Luis
 */
public interface CrudController<T, ID> {

    @GetMapping
    ResponseEntity<List<T>> listAll();

    @GetMapping("/{id}")
    ResponseEntity<T> getById(@PathVariable ID id);

    @PostMapping
    ResponseEntity<T> create(@RequestBody T dto);

    @PutMapping("/{id}")
    ResponseEntity<T> update(@PathVariable ID id, @RequestBody T dto);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable ID id);
}
