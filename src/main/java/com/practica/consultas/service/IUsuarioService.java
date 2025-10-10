package com.practica.consultas.service;

import com.practica.consultas.model.Usuario;
import com.practica.consultas.service.generic.ICrudService;

/**
 *
 * @author Luis
 */
public interface IUsuarioService extends ICrudService<Usuario, Long> {

    Usuario findByCorreo(String correo);
}
