package com.practica.consultas.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El formato del correo no es válido.")
        @Size(max = 50, message = "El correo no puede tener más de 50 caracteres.")
        String correo,
        
        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres.")
        String password,
        
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres.")
        String nombre) {

}
