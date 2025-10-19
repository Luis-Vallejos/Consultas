package com.practica.consultas.service.impl;

import com.practica.consultas.exceptions.ReglaNegocioException;
import com.practica.consultas.model.Role;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.RoleRepository;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.service.IAuthService;
import com.practica.consultas.utils.JwtUtil;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luis
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public Usuario register(String correo, String password, String nombre) {
        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new ReglaNegocioException("El correo ya está registrado.");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Error de configuración: No existe el rol USER"));

        Usuario usuario = Usuario.builder()
                .correo(correo)
                .contrasenia(passwordEncoder.encode(password))
                .nombre(nombre)
                .roles(Collections.singleton(roleUser))
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public String login(String correo, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(correo, password)
        );

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ReglaNegocioException("Usuario no encontrado tras una autenticación exitosa."));

        return jwtUtil.generateToken(usuario);
    }
}
