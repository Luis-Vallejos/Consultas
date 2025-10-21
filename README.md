Sistema de Reserva de Salas - API

API RESTful para la gestión y reserva de salas de reuniones, desarrollada con Spring Boot, Spring Security, JWT y MySQL.

Características Principales

Autenticación y Autorización: Sistema seguro basado en roles (ADMIN, USER) utilizando JWT.

Gestión de Salas (CRUD): Creación, lectura, actualización y eliminación de salas (protegido para ADMIN).

Gestión de Equipos (CRUD): Administración del equipamiento disponible en las salas (protegido para ADMIN).

Sistema de Reservas: Los usuarios autenticados pueden crear, cancelar y ver sus reservas.

Búsqueda Avanzada: Filtra salas disponibles por fecha, capacidad y equipamiento.

Concurrencia: Mecanismo de bloqueo para evitar reservas simultáneas en la misma sala.

Base de Datos: Persistencia de datos con MySQL y migraciones gestionadas por Flyway.

Contenerización: Totalmente dockerizado para un despliegue y desarrollo sencillos con Docker Compose.

Guía de Inicio Rápido (5 minutos)

Sigue estos pasos para tener el proyecto corriendo localmente usando Docker.

Pre-requisitos

Docker

Docker Compose

Java 17+

Maven 3.8+

Pasos

1. Clonar el Repositorio

git clone https://github.com/luis-vallejos/Consultas.git
cd Consultas


2. Empaquetar la Aplicación

Este comando compila el código, ejecuta las pruebas y crea el archivo .jar que se usará en el contenedor Docker.

./mvnw clean package -DskipTests


3. Levantar los Servicios con Docker Compose

Este único comando construirá la imagen de la aplicación y levantará dos contenedores: uno para la API (app) y otro para la base de datos MySQL (db).

docker-compose up --build


¡Y listo! La base de datos se iniciará, Flyway aplicará las migraciones y la aplicación estará disponible.

API URL: http://localhost:8080

Endpoint de Salud: http://localhost:8080/actuator/health (debería responder {"status":"UP"}).

Detener la Aplicación

Para detener y eliminar los contenedores, presiona Ctrl + C en la terminal donde ejecutaste docker-compose up y luego ejecuta:

docker-compose down


Documentación de la API

La API sigue un diseño RESTful estándar. Todas las respuestas son en formato JSON.

Autenticación

Todas las rutas (excepto /api/auth/**) requieren un Token JWT en la cabecera de autorización.

Authorization: Bearer <TU_TOKEN_JWT>

POST /api/auth/register

Registra un nuevo usuario en el sistema.

Body (Request):

{
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@example.com",
  "password": "password123"
}


Response (201 Created):

{
  "id": 4,
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@example.com",
  "contrasenia": "$2a$10$...",
  "roles": [{"id": 2, "name": "ROLE_USER"}]
}


POST /api/auth/login

Autentica a un usuario y devuelve un token JWT.

Body (Request):

{
  "correo": "admin@consultas.com",
  "password": "admin"
}


Response (200 OK):

{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}


Salas

GET /api/salas

Busca y lista todas las salas con paginación.

Query Params (Opcionales):

capacidadMinima (int): Filtra por capacidad mínima.

tipoEquipo (string): Filtra por tipo de equipo disponible.

activa (boolean): Filtra por salas activas o inactivas.

page, size, sort: Parámetros de paginación de Spring Data.

Response (200 OK):

{
  "content": [
    {
      "id": 1,
      "nombre": "Sala Creativa",
      "capacidad": 12,
      "ubicacion": "Piso 1, Ala Norte",
      "activa": true,
      "equipos": []
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "number": 0
}


GET /api/salas/disponibles

Busca salas disponibles en un rango de fechas y con filtros adicionales.

Query Params (Obligatorios):

inicio (string): Fecha y hora de inicio en formato ISO (YYYY-MM-DDTHH:mm:ss).

fin (string): Fecha y hora de fin en formato ISO.

Query Params (Opcionales):

capacidad (int): Capacidad mínima requerida.

equipoIds (long[]): Lista de IDs de equipos requeridos.

Ejemplo de Petición:
GET /api/salas/disponibles?inicio=2025-12-01T10:00:00&fin=2025-12-01T12:00:00&capacidad=5

Response (200 OK): Lista de SalaDto

POST /api/salas

Crea una nueva sala. (Requiere rol ADMIN)

Body (Request):

{
  "nombre": "Sala de Innovación",
  "capacidad": 20,
  "ubicacion": "Piso 3",
  "activa": true,
  "equipoIds": [1, 3]
}


Response (200 OK): SalaDto de la sala creada.

Reservas

POST /api/reservas

Crea una nueva reserva para el usuario autenticado.

Body (Request):

{
  "salaId": 1,
  "inicio": "2025-12-25T14:00:00",
  "fin": "2025-12-25T15:30:00"
}


Response (201 Created): ReservaDto de la reserva creada.

GET /api/reservas

Obtiene la lista de reservas del usuario autenticado. Si el usuario es ADMIN, obtiene todas las reservas.

Response (200 OK): Lista de ReservaDto

PUT /api/reservas/{id}/cancelar

Cancela una reserva existente.

Response (200 OK): ReservaDto con el estado "CANCELADA".

Historial de Cambios

Versión 1.0.0 (2025-10-21)

🎉 Lanzamiento inicial del proyecto.

Funcionalidades:

Módulos completos de Autenticación, Salas, Equipos y Reservas.

Soporte para roles de ADMIN y USER.

Búsqueda de salas disponibles con filtros.

Configuración de Docker para despliegue fácil.

Migraciones de base de datos con Flyway.

Pruebas unitarias y de integración (smoke test).