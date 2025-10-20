package com.practica.consultas.service;

import com.practica.consultas.exceptions.ReglaNegocioException;
import com.practica.consultas.model.Reserva;
import com.practica.consultas.model.Sala;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.ReservaRepository;
import com.practica.consultas.repository.SalaRepository;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.service.ISalaLockService;
import com.practica.consultas.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private SalaRepository salaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ISalaLockService salaLockService;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Usuario usuario;
    private Sala sala;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).correo("test@example.com").nombre("Test User").build();
        sala = Sala.builder().id(1L).nombre("Sala Test").build();

        // Comportamiento por defecto para el bloqueo de sala
        lenient().when(salaLockService.runWithLock(anyLong(), any()))
                .thenAnswer(invocation -> {
                    Supplier<Reserva> supplier = invocation.getArgument(1);
                    return supplier.get();
                });
    }

    @Test
    @DisplayName("Debe permitir crear una reserva justo cuando termina otra")
    void crearReservaAlLimite() {
        // Arrange
        LocalDateTime finReservaAnterior = LocalDateTime.now().plusHours(2);
        LocalDateTime inicioNuevaReserva = finReservaAnterior;
        LocalDateTime finNuevaReserva = inicioNuevaReserva.plusHours(1);

        ReservaRequest request = new ReservaRequest(sala.getId(), inicioNuevaReserva, finNuevaReserva);

        when(authentication.getName()).thenReturn(usuario.getCorreo());
        when(usuarioRepository.findByCorreo(usuario.getCorreo())).thenReturn(Optional.of(usuario));
        when(salaRepository.findById(sala.getId())).thenReturn(Optional.of(sala));
        when(reservaRepository.existsBySalaAndFechas(sala.getId(), inicioNuevaReserva, finNuevaReserva)).thenReturn(false);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Reserva nuevaReserva = reservaService.crearReserva(request, authentication);

        // Assert
        assertNotNull(nuevaReserva);
        assertEquals(inicioNuevaReserva, nuevaReserva.getInicio());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe rechazar reserva de menos de 30 minutos")
    void rechazarReservaCorta() {
        // Arrange
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
        LocalDateTime fin = inicio.plusMinutes(29);
        ReservaRequest request = new ReservaRequest(1L, inicio, fin);

        // Act & Assert
        ReglaNegocioException exception = assertThrows(ReglaNegocioException.class, () -> {
            reservaService.crearReserva(request, authentication);
        });

        assertEquals("La duración mínima de la reserva es de 30 minutos.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Debe rechazar cancelación 59 minutos antes del inicio")
    void rechazarCancelacionTardia() {
        // Arrange
        LocalDateTime inicioReserva = LocalDateTime.now().plusMinutes(59);
        Reserva reservaExistente = Reserva.builder()
                .id(1L)
                .usuario(usuario)
                .sala(sala)
                .inicio(inicioReserva)
                .fin(inicioReserva.plusHours(1))
                .estado("CONFIRMADA")
                .build();

        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaExistente));
        when(authentication.getName()).thenReturn(usuario.getCorreo());
        when(authentication.getAuthorities()).thenReturn(java.util.Collections.emptyList()); // No es admin

        // Act & Assert
        ReglaNegocioException exception = assertThrows(ReglaNegocioException.class, () -> {
            reservaService.cancelarReserva(1L, authentication);
        });

        assertEquals("Solo puede cancelar la reserva hasta 1 hora antes de su inicio.", exception.getMessage());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}
