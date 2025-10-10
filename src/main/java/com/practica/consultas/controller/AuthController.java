package com.practica.consultas.controller;

import com.practica.consultas.model.Usuario;
import com.practica.consultas.request.LoginRequest;
import com.practica.consultas.request.RegisterRequest;
import com.practica.consultas.response.TokenResponse;
import com.practica.consultas.service.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody RegisterRequest request) {
        Usuario usuario = authService.register(request.correo(), request.password(), request.nombre());
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request.correo(), request.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }

}
