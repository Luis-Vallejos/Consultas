package com.practica.consultas.repository;

import com.practica.consultas.model.Reserva;
import com.practica.consultas.model.Sala;
import com.practica.consultas.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@DisplayName("Pruebas de Datos para ReservaRepository (CRUD)")
class ReservaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    private Usuario usuario;
    private Sala sala;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().nombre("Usuario de Prueba").correo("test@example.com").contrasenia("123456").build();
        entityManager.persist(usuario);

        sala = Sala.builder().nombre("Sala de Test").capacidad(10).ubicacion("Piso 1").activa(true).build();
        entityManager.persist(sala);
        entityManager.flush();
    }

    @Test
    @DisplayName("1. CREAR y LEER una reserva")
    void deberiaCrearYLeerUnaReserva() {
        Reserva nuevaReserva = Reserva.builder()
                .usuario(usuario)
                .sala(sala)
                .inicio(LocalDateTime.now().plusDays(1))
                .fin(LocalDateTime.now().plusDays(1).plusHours(1))
                .estado("CONFIRMADA")
                .build();

        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        Reserva reservaEncontrada = reservaRepository.findById(reservaGuardada.getId()).orElse(null);
        assertThat(reservaEncontrada).isNotNull();
        assertThat(reservaEncontrada.getSala().getNombre()).isEqualTo("Sala de Test");
    }

    @Test
    @DisplayName("2. ACTUALIZAR una reserva existente")
    void deberiaActualizarUnaReserva() {
        Reserva reservaInicial = Reserva.builder()
                .usuario(usuario)
                .sala(sala)
                .inicio(LocalDateTime.now().plusDays(2))
                .fin(LocalDateTime.now().plusDays(2).plusHours(1))
                .estado("CONFIRMADA")
                .build();
        entityManager.persist(reservaInicial);
        entityManager.flush();

        Reserva reservaParaActualizar = reservaRepository.findById(reservaInicial.getId()).get();
        reservaParaActualizar.setEstado("CANCELADA");
        reservaRepository.save(reservaParaActualizar);
        entityManager.flush();

        Reserva reservaActualizada = reservaRepository.findById(reservaInicial.getId()).get();
        assertThat(reservaActualizada.getEstado()).isEqualTo("CANCELADA");
    }

    @Test
    @DisplayName("3. BORRAR una reserva existente")
    void deberiaEliminarUnaReserva() {
        Reserva reservaParaBorrar = Reserva.builder()
                .usuario(usuario)
                .sala(sala)
                .inicio(LocalDateTime.now().plusDays(3))
                .fin(LocalDateTime.now().plusDays(3).plusHours(1))
                .estado("CONFIRMADA")
                .build();
        entityManager.persist(reservaParaBorrar);
        entityManager.flush();
        Long idReserva = reservaParaBorrar.getId();

        reservaRepository.deleteById(idReserva);
        entityManager.flush();

        Optional<Reserva> reservaEliminada = reservaRepository.findById(idReserva);
        assertThat(reservaEliminada).isNotPresent();
    }
}
