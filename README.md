Perfecto 👍 Aquí tienes tu **README** completamente formateado en **Markdown**, listo para subirlo a GitHub:

---

# 🏢 Sistema de Reserva de Salas - API

API RESTful para la **gestión y reserva de salas de reuniones**, desarrollada con **Spring Boot**, **Spring Security**, **JWT** y **MySQL**.

---

## 🚀 Características Principales

* 🔐 **Autenticación y Autorización:** Sistema seguro basado en roles (`ADMIN`, `USER`) utilizando JWT.
* 🏠 **Gestión de Salas (CRUD):** Creación, lectura, actualización y eliminación de salas (protegido para ADMIN).
* 💻 **Gestión de Equipos (CRUD):** Administración del equipamiento disponible en las salas (protegido para ADMIN).
* 📅 **Sistema de Reservas:** Los usuarios autenticados pueden crear, cancelar y ver sus reservas.
* 🔎 **Búsqueda Avanzada:** Filtra salas disponibles por fecha, capacidad y equipamiento.
* ⚙️ **Concurrencia:** Mecanismo de bloqueo para evitar reservas simultáneas en la misma sala.
* 🗄️ **Base de Datos:** Persistencia con **MySQL** y migraciones gestionadas por **Flyway**.
* 🐳 **Contenerización:** Totalmente dockerizado para un despliegue y desarrollo sencillos con **Docker Compose**.

---

## 🧭 Guía de Inicio Rápido (5 minutos)

Sigue estos pasos para tener el proyecto corriendo localmente usando Docker.

### 🧩 Pre-requisitos

* Docker
* Docker Compose
* Java 17+
* Maven 3.8+

---

### 🪄 Pasos

#### 1️⃣ Clonar el Repositorio

```bash
git clone https://github.com/luis-vallejos/Consultas.git
cd Consultas
```

#### 2️⃣ Empaquetar la Aplicación

Compila el código, ejecuta las pruebas y crea el archivo `.jar` que se usará en Docker.

```bash
./mvnw clean package -DskipTests
```

#### 3️⃣ Levantar los Servicios con Docker Compose

Construye la imagen y levanta los contenedores (API y MySQL).

```bash
docker-compose up --build
```

✅ **Listo:**
La base de datos se iniciará, Flyway aplicará las migraciones y la aplicación estará disponible en:

* **API URL:** [http://localhost:8080](http://localhost:8080)
* **Endpoint de Salud:** [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health) → `{"status":"UP"}`

---

### 🛑 Detener la Aplicación

Presiona `Ctrl + C` donde se ejecutó Docker y luego:

```bash
docker-compose down
```

---

## 📘 Documentación de la API

La API sigue un diseño **RESTful** estándar.
Todas las respuestas son en formato **JSON**.

---

### 🔐 Autenticación

Todas las rutas (excepto `/api/auth/**`) requieren un **Token JWT** en la cabecera:

```
Authorization: Bearer <TU_TOKEN_JWT>
```

#### 📤 POST /api/auth/register

Registra un nuevo usuario.

**Body (Request):**

```json
{
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@example.com",
  "password": "password123"
}
```

**Response (201 Created):**

```json
{
  "id": 4,
  "nombre": "Carlos Ruiz",
  "correo": "carlos.ruiz@example.com",
  "contrasenia": "$2a$10$...",
  "roles": [{"id": 2, "name": "ROLE_USER"}]
}
```

---

#### 🔑 POST /api/auth/login

Autentica un usuario y devuelve un token JWT.

**Body (Request):**

```json
{
  "correo": "admin@consultas.com",
  "password": "admin"
}
```

**Response (200 OK):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 🏠 Salas

#### 📥 GET /api/salas

Lista todas las salas con paginación.

**Query Params (opcionales):**

* `capacidadMinima` *(int)* — Filtra por capacidad mínima
* `tipoEquipo` *(string)* — Filtra por tipo de equipo
* `activa` *(boolean)* — Filtra por salas activas/inactivas
* `page`, `size`, `sort` — Paginación estándar de Spring Data

**Response (200 OK):**

```json
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
```

---

#### 🕓 GET /api/salas/disponibles

Busca salas disponibles en un rango de fechas.

**Query Params (obligatorios):**

* `inicio`: Fecha/hora inicio (ISO: `YYYY-MM-DDTHH:mm:ss`)
* `fin`: Fecha/hora fin (ISO)

**Opcionales:**

* `capacidad` *(int)*
* `equipoIds` *(long[])*

**Ejemplo:**

```
GET /api/salas/disponibles?inicio=2025-12-01T10:00:00&fin=2025-12-01T12:00:00&capacidad=5
```

**Response (200 OK):** Lista de `SalaDto`.

---

#### ➕ POST /api/salas

Crea una nueva sala (**rol ADMIN requerido**).

**Body (Request):**

```json
{
  "nombre": "Sala de Innovación",
  "capacidad": 20,
  "ubicacion": "Piso 3",
  "activa": true,
  "equipoIds": [1, 3]
}
```

**Response (200 OK):** `SalaDto` creada.

---

## 📅 Reservas

#### ➕ POST /api/reservas

Crea una nueva reserva.

**Body (Request):**

```json
{
  "salaId": 1,
  "inicio": "2025-12-25T14:00:00",
  "fin": "2025-12-25T15:30:00"
}
```

**Response (201 Created):** `ReservaDto` creada.

---

#### 📄 GET /api/reservas

Obtiene las reservas del usuario autenticado (ADMIN ve todas).

**Response (200 OK):** Lista de `ReservaDto`.

---

#### ❌ PUT /api/reservas/{id}/cancelar

Cancela una reserva existente.

**Response (200 OK):** `ReservaDto` con estado `"CANCELADA"`.

---

## 🕓 Historial de Cambios

### 🧩 Versión 1.0.0 (2025-10-21)

🎉 **Lanzamiento inicial del proyecto**

**Incluye:**

* Módulos completos de Autenticación, Salas, Equipos y Reservas
* Roles de `ADMIN` y `USER`
* Búsqueda avanzada de salas
* Configuración Docker para despliegue rápido
* Migraciones con Flyway
* Pruebas unitarias y de integración (smoke test)

---
