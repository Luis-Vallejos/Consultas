package com.practica.consultas.controller;

import com.practica.consultas.model.Usuario;
import com.practica.consultas.request.LoginRequest;
import com.practica.consultas.request.RegisterRequest;
import com.practica.consultas.response.TokenResponse;
import com.practica.consultas.service.IAuthService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 *
 * @author Luis
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@Valid @RequestBody RegisterRequest request) {
        Usuario usuario = authService.register(request.correo(), request.password(), request.nombre());

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/usuarios/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request.correo(), request.password());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
