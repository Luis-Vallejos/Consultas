---

# Documentación del Frontend (Thymeleaf + JavaScript)

Este documento describe la arquitectura del frontend, el manejo de la seguridad y el flujo de integración con el backend.

---

## 1. Estructura de Vistas

El frontend utiliza **Thymeleaf** para el renderizado del lado del servidor, pero funciona de manera similar a una aplicación de una sola página (SPA) en su interacción con la API.

* **`WebController.java`**
  Controlador de Spring responsable de servir todas las plantillas HTML base.
  No pasa modelos de datos (excepto el título de la página).

* **`templates/layout/layout.html`**
  Plantilla principal que define:

  * `<head>` con dependencias (Bootstrap, CSS, JS, etc.)
  * `<header>` con la barra de navegación.
  * `<footer>` con la información común.
    Todas las demás vistas se insertan dentro del elemento `<main>` de este layout.

* **Vistas de Página** (`home.html`, `salas.html`, `buscar.html`, etc.)
  Son “caparazones” HTML que extienden `layout.html`.
  Contienen la estructura inicial y un bloque `<script>` que proporciona toda la interactividad mediante JavaScript.

---

## 2. Flujo de Integración (Datos)

El flujo de datos entre el cliente y el servidor sigue este proceso:

1. **Solicitud HTTP:**
   El usuario navega a `/salas`.

2. **Spring (Servidor):**
   `WebController` intercepta la solicitud y devuelve el archivo `salas.html` renderizado por Thymeleaf.

3. **Navegador (Cliente):**
   El navegador carga `salas.html`.

4. **JavaScript (Cliente):**
   Se dispara el evento `DOMContentLoaded`.

5. **Llamada a la API:**
   El script ejecuta:

   ```javascript
   fetch('/api/salas')
   ```

   para obtener los datos reales.

6. **Spring (Servidor):**
   `SalaController` intercepta `/api/salas`, consulta la base de datos y devuelve una respuesta JSON.

7. **JavaScript (Cliente):**
   El script recibe el JSON y lo usa para renderizar dinámicamente el contenido en la página, por ejemplo:

   ```javascript
   renderSalas(data.content);
   ```

---

## 3. Manejo de Seguridad (JWT)

La seguridad del frontend se gestiona completamente del lado del cliente usando **JavaScript** y **localStorage**.

### 3.1. Inicio de Sesión

* **Login:**
  En `login.html`, el `fetch` a `/api/auth/login` recibe un token JWT.

* **Almacenamiento:**
  El token se guarda en:

  ```javascript
  localStorage.setItem("jwtToken", data.token);
  ```

* **Inyección de Token:**
  En las vistas que requieren autenticación (`salas.html`, `buscar.html`, `admin.html`, etc.), el token se recupera desde el almacenamiento local.

* **Llamadas Seguras:**
  Todas las peticiones protegidas deben incluir el token en la cabecera:

  ```javascript
  fetch("/api/salas", {
      headers: {
          "Authorization": `Bearer ${token}`
      }
  });
  ```

---

## 3.2. Vistas por Roles

### Decodificación de JWT

`layout.html` contiene una función global que decodifica el payload del JWT:

```javascript
function parseJwt(token) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    return JSON.parse(window.atob(base64));
}
```

### Comprobación de Rol

Una segunda función, `checkAdminStatus(token)`, usa `parseJwt` para leer el array `roles` dentro del payload y verificar si incluye `ROLE_ADMIN`.

### Renderizado Condicional

El script principal de `layout.html` usa `checkAdminStatus(token)` para mostrar u ocultar el enlace con `id="nav-admin-li"`.

### Protección de Ruta (Cliente)

En vistas como `admin.html`, se repite esta comprobación al cargar.
Si el usuario **no es administrador**, se redirige automáticamente a la página de inicio (`/`).

