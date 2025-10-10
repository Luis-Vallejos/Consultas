package com.practica.consultas.service.impl;

import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.service.IUsuarioService;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public ArrayList<Usuario> findAll() {
        return (ArrayList<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public Usuario create(Usuario entity) {
        return usuarioRepository.save(entity);
    }

    @Override
    public Usuario edit(Long id, Usuario updated) {
        Usuario usuario = findById(id);
        if (usuario == null) {
            return null;
        }
        usuario.setNombre(updated.getNombre());
        usuario.setCorreo(updated.getCorreo());
        usuario.setContrasenia(updated.getContrasenia());
        usuario.setRoles(updated.getRoles());
        return usuarioRepository.save(usuario);
    }

    @Override
    public boolean delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            return false;
        }
        usuarioRepository.deleteById(id);
        return true;
    }
}
