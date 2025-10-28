package com.practica.consultas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador para servir las páginas web (vistas HTML) de la aplicación.
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String showHomePage() {
        return "home"; // Busca /templates/home.html
    }

    /**
     * Nuevo método para mostrar la página de inicio de sesión.
     *
     * @return El nombre de la plantilla "login"
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Busca /templates/login.html
    }

}
