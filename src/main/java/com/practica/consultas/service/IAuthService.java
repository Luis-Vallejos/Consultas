package com.practica.consultas.service;

import com.practica.consultas.model.Usuario;

/**
 *
 * @author Luis
 */
public interface IAuthService {

    Usuario register(String correo, String password, String nombre);

    String login(String correo, String password);
}
