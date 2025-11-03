package com.practica.consultas;

import com.fasterxml.jackson.databind.JsonNode;
import com.practica.consultas.model.Role;
import com.practica.consultas.model.Usuario;
import com.practica.consultas.repository.RoleRepository;
import com.practica.consultas.repository.UsuarioRepository;
import com.practica.consultas.request.LoginRequest;
import com.practica.consultas.request.RegisterRequest;
import com.practica.consultas.request.ReservaRequest;
import com.practica.consultas.request.SalaRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DisplayName("Prueba de Humo del Flujo Completo de la API")
// (Solución) Se elimina @TestInstance y @Order para que las pruebas sean independientes
public class ApiSmokeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    // (Solución) Se eliminan las variables de instancia compartidas
    // private String userToken;
    // private long salaId;
    // private String userEmail;
    // private String userPassword;
    @BeforeAll
    static void setup(@Autowired RoleRepository roleRepository,
            @Autowired UsuarioRepository usuarioRepository,
            @Autowired BCryptPasswordEncoder passwordEncoder) {

        // 1. Crear Roles si no existen
        Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(()
                -> roleRepository.save(Role.builder().name("ROLE_USER").build())
        );
        Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElseGet(()
                -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build())
        );

        // 2. Crear usuario Administrador si no existe
        if (usuarioRepository.findByCorreo("admin@consultas.com").isEmpty()) {
            Usuario admin = Usuario.builder()
                    .nombre("Admin de Prueba")
                    .correo("admin@consultas.com")
                    .contrasenia(passwordEncoder.encode("admin"))
                    .roles(Set.of(roleAdmin))
                    .build();
            usuarioRepository.save(admin);
        }
    }

    @Test
    @DisplayName("Flujo completo: Registrar -> Login -> Crear Sala -> Listar -> Reservar -> Ver Reservas -> Cancelar")
    void smokeTestFlujoCompleto() {
        // --- Datos de prueba ---
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String userEmail = "smoketest-" + uniqueId + "@example.com";
        String userPassword = "password123";

        // === PASO 1: Registrar un nuevo usuario ===
        RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, "Smoke Test User");
        ResponseEntity<String> responseRegister = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);
        assertThat(responseRegister.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        System.out.println("✅ PASO 1: Usuario registrado exitosamente: " + userEmail);

        // === PASO 2: Iniciar sesión como ADMIN para obtener token ===
        LoginRequest loginAdminRequest = new LoginRequest("admin@consultas.com", "admin");
        ResponseEntity<JsonNode> responseLoginAdmin = restTemplate.postForEntity("/api/auth/login", loginAdminRequest, JsonNode.class);
        assertThat(responseLoginAdmin.getStatusCode()).isEqualTo(HttpStatus.OK);
        String adminToken = responseLoginAdmin.getBody().get("token").asText();
        assertThat(adminToken).isNotBlank();
        System.out.println("✅ PASO 2: Token de ADMIN obtenido.");

        // === PASO 3: Crear una nueva sala (como ADMIN) ===
        SalaRequest salaRequest = new SalaRequest("Sala de Humo " + uniqueId, 10, "Piso de Pruebas", true, Collections.emptySet());
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setBearerAuth(adminToken);
        HttpEntity<SalaRequest> requestSalaEntity = new HttpEntity<>(salaRequest, adminHeaders);
        ResponseEntity<JsonNode> responseSala = restTemplate.postForEntity("/api/salas", requestSalaEntity, JsonNode.class);
        assertThat(responseSala.getStatusCode()).isEqualTo(HttpStatus.OK);
        long salaId = responseSala.getBody().get("id").asLong(); // (Solución) Variable local
        assertThat(salaId).isPositive();
        System.out.println("✅ PASO 3: Sala creada exitosamente con ID: " + salaId);

        // === PASO 4: Iniciar sesión como el nuevo usuario y listar salas ===
        LoginRequest loginUserRequest = new LoginRequest(userEmail, userPassword);
        ResponseEntity<JsonNode> responseLoginUser = restTemplate.postForEntity("/api/auth/login", loginUserRequest, JsonNode.class);
        assertThat(responseLoginUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        String userToken = responseLoginUser.getBody().get("token").asText(); // (Solución) Variable local
        assertThat(userToken).isNotBlank();
        System.out.println("✅ PASO 4.1: Token del nuevo usuario obtenido.");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<String> listSalasEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<String> responseSalas = restTemplate.exchange("/api/salas", HttpMethod.GET, listSalasEntity, String.class);
        assertThat(responseSalas.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("✅ PASO 4.2: Listado de salas obtenido correctamente.");

        // === PASO 5: Reservar la sala creada ===
        LocalDateTime inicio = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime fin = inicio.plusHours(1);
        ReservaRequest reservaRequest = new ReservaRequest(salaId, inicio, fin);
        HttpEntity<ReservaRequest> requestReservaEntity = new HttpEntity<>(reservaRequest, userHeaders);
        ResponseEntity<JsonNode> responseReserva = restTemplate.postForEntity("/api/reservas", requestReservaEntity, JsonNode.class);
        assertThat(responseReserva.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        long reservaId = responseReserva.getBody().get("id").asLong();
        assertThat(reservaId).isPositive();
        System.out.println("✅ PASO 5: Reserva creada exitosamente con ID: " + reservaId);

        // === PASO 6: Ver mis reservas y confirmar la creación ===
        HttpEntity<String> listReservasEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<JsonNode> responseMisReservas = restTemplate.exchange("/api/reservas", HttpMethod.GET, listReservasEntity, JsonNode.class);
        assertThat(responseMisReservas.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseMisReservas.getBody().toString()).contains("\"id\":" + reservaId);
        System.out.println("✅ PASO 6: La nueva reserva fue encontrada en la lista de 'mis reservas'.");

        // === PASO 7: Cancelar la reserva ===
        HttpEntity<String> cancelarReservaEntity = new HttpEntity<>(userHeaders);
        ResponseEntity<JsonNode> responseCancelar = restTemplate.exchange("/api/reservas/{id}/cancelar", HttpMethod.PUT, cancelarReservaEntity, JsonNode.class, reservaId);
        assertThat(responseCancelar.getStatusCode()).isEqualTo(HttpStatus.OK);
        String estado = responseCancelar.getBody().get("estado").asText();
        assertThat(estado).isEqualTo("CANCELADA");
        System.out.println("✅ PASO 7: La reserva fue cancelada exitosamente.");
    }

    @Test
    @DisplayName("Usuario ROLE_USER no debe poder crear salas (Debe dar 403 Forbidden)")
    void userCannotCreateSala() {
        // (Solución) 1. Configuración de usuario independiente
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String userEmail = "test-user-sala-" + uniqueId + "@example.com";
        String userPassword = "password123";
        RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, "Test User Sala");
        restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);

        // (Solución) 2. Iniciar sesión como ese usuario
        LoginRequest loginUserRequest = new LoginRequest(userEmail, userPassword);
        ResponseEntity<JsonNode> responseLoginUser = restTemplate.postForEntity("/api/auth/login", loginUserRequest, JsonNode.class);
        String userToken = responseLoginUser.getBody().get("token").asText();
        assertThat(userToken).as("El token de usuario no debe ser nulo").isNotBlank();

        // 3. Intentar crear una sala (Acción de Admin)
        SalaRequest salaRequest = new SalaRequest("Sala Prohibida", 5, "Sótano", true, Collections.emptySet());
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<SalaRequest> requestSalaEntity = new HttpEntity<>(salaRequest, userHeaders);

        ResponseEntity<String> responseSala = restTemplate.postForEntity("/api/salas", requestSalaEntity, String.class);

        // 4. Verificar que da 403 Forbidden
        assertThat(responseSala.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        System.out.println("✅ PRUEBA DE SEGURIDAD: Usuario ROLE_USER recibió 403 (Forbidden) al intentar crear sala.");
    }

    // --- (Solución) PRUEBA DE SEGURIDAD INDEPENDIENTE ---
    @Test
    @DisplayName("Usuario ROLE_USER no debe poder eliminar equipos (Debe dar 403 Forbidden)")
    void userCannotDeleteEquipo() {
        // (Solución) 1. Configuración de usuario independiente
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        String userEmail = "test-user-equipo-" + uniqueId + "@example.com";
        String userPassword = "password123";
        RegisterRequest registerRequest = new RegisterRequest(userEmail, userPassword, "Test User Equipo");
        restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);

        // (Solución) 2. Iniciar sesión como ese usuario
        LoginRequest loginUserRequest = new LoginRequest(userEmail, userPassword);
        ResponseEntity<JsonNode> responseLoginUser = restTemplate.postForEntity("/api/auth/login", loginUserRequest, JsonNode.class);
        String userToken = responseLoginUser.getBody().get("token").asText();
        assertThat(userToken).as("El token de usuario no debe ser nulo").isNotBlank();

        // 3. Intentar eliminar un equipo (ID 1, creado por V1__init.sql)
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(userHeaders);

        // (ID 1 debe existir de las migraciones V1)
        ResponseEntity<String> responseDelete = restTemplate.exchange("/api/equipos/1", HttpMethod.DELETE, requestEntity, String.class);

        // 4. Verificar que da 403 Forbidden
        assertThat(responseDelete.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        System.out.println("✅ PRUEBA DE SEGURIDAD: Usuario ROLE_USER recibió 403 (Forbidden) al intentar eliminar equipo.");
    }
    // --- FIN DE LA SOLUCIÓN ---
}
