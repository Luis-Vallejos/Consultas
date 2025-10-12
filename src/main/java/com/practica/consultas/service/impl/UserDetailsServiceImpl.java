package com.practica.consultas.service.impl;

import com.practica.consultas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscamos el usuario por su correo electrónico
        return usuarioRepository.findByCorreo(username)
                .map(usuario -> {
                    // Mapeamos los roles del usuario a SimpleGrantedAuthority
                    var authorities = usuario.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList());

                    // Creamos y devolvemos un objeto User de Spring Security
                    return new User(
                            usuario.getCorreo(),
                            usuario.getContrasenia(),
                            authorities
                    );
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo: " + username));
    }
}
