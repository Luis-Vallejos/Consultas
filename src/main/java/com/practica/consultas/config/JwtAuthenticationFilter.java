package com.practica.consultas.config;

import com.practica.consultas.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Obtener el encabezado "Authorization" de la petición.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Si el encabezado no existe o no empieza con "Bearer ", continuar con el siguiente filtro.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer el token JWT (quitando el prefijo "Bearer ").
        jwt = authHeader.substring(7);
        userEmail = jwtUtil.extractUsername(jwt); // Extraemos el email del usuario del token.

        // 4. Si tenemos el email y el usuario aún no está autenticado en el contexto de seguridad...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // ... cargamos los detalles del usuario desde la base de datos.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 5. Validamos si el token corresponde al usuario y no ha expirado.
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                // Si es válido, creamos un token de autenticación.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Las credenciales son nulas porque ya validamos el token.
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 6. Establecemos la autenticación en el contexto de seguridad.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // 7. Continuamos con la cadena de filtros.
        filterChain.doFilter(request, response);
    }
}
