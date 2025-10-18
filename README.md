Proyecto Consultas - Sistema de Reserva de Salas

Este es un sistema de API REST para la gestión y reserva de salas de reuniones, desarrollado con Spring Boot.

Requisitos

Java 17 o superior

Maven 3.8 o superior

Docker

Docker Compose

Levantamiento Rápido (Recomendado con Docker)

Este método levanta tanto la base de datos como la aplicación con un solo comando, asegurando un entorno limpio y consistente.

Paso 1: Empaquetar la aplicación

Desde la raíz del proyecto, ejecuta el siguiente comando para generar el archivo .jar.

./mvnw clean package -DskipTests


Paso 2: Levantar los servicios

Una vez que el empaquetado sea exitoso, usa Docker Compose para construir y levantar los contenedores.

docker-compose up --build


La aplicación estará disponible en http://localhost:8080.

Endpoints Clave

API Docs (Swagger/OpenAPI si se implementa): http://localhost:8080/swagger-ui.html

Endpoint de Salud: http://localhost:8080/actuator/health (Devolverá {"status":"UP"} si todo está bien)

API Base: http://localhost:8080/api/...

Usuarios de Demo

Puedes usar los siguientes usuarios para probar la API:

Correo

Contraseña

Rol

admin@consultas.com

admin

ROLE_ADMIN

user@consultas.com

user

ROLE_USER

ana.torres@example.com

user

ROLE_USER

carlos.ruiz@example.com

user

ROLE_USER

Detener la Aplicación

Para detener y eliminar los contenedores, ejecuta:

docker-compose down
