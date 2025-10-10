package com.practica.consultas.request;

/**
 *
 * @author Luis
 */
public record RegisterRequest(
        String correo,
        String password,
        String nombre) {

}
