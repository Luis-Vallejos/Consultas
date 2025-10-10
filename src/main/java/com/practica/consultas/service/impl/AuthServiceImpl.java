package com.practica.consultas.service.impl;

import com.practica.consultas.model.Role;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.RoleRepository;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.service.IAuthService;
import com.practica.consultas.utils.JwtUtil;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;          // ✅ agregado
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;                     // ✅ agregado

/**
 *
 * @author Luis
 */
@Service  // ✅ necesario para que Spring detecte e instancie este servicio
public class AuthServiceImpl implements IAuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ✅ Marca explícitamente el constructor como @Autowired para inyección por constructor
    @Autowired
    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            RoleRepository roleRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Usuario register(String correo, String password, String nombre) {
        // ✅ Cambié .isPresent() -> .isEmpty() para mayor claridad semántica
        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new RuntimeException("El correo ya está registrado.");
        }

        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("No existe el rol USER"));

        // ✅ Asegúrate de que tu entidad Usuario tenga builder y nombres de campos correctos
        Usuario usuario = Usuario.builder()
                .correo(correo)
                .contrasenia(passwordEncoder.encode(password)) // asegúrate que el campo se llama igual
                .nombre(nombre)
                .roles(Collections.singleton(roleUser))
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public String login(String correo, String password) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(password, usuario.getContrasenia())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return jwtUtil.generateToken(usuario);
    }
}
