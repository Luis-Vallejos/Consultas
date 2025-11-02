package com.practica.consultas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @GetMapping("/salas")
    public String showSalasPage() {
        return "salas";
    }

    @GetMapping("/buscar")
    public String showSearchPage() {
        return "buscar";
    }

    @GetMapping("/sala-detalle")
    public String showSalaDetallePage() {
        return "sala-detalle";
    }

    @GetMapping("/mis-reservas")
    public String showMisReservasPage() {
        return "mis-reservas";
    }
}
