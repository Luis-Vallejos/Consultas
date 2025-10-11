-- ==========================================================
-- SCRIPT DE MIGRACIÓN INICIAL - PROYECTO "CONSULTAS"
-- Versión actualizada para coincidir con los modelos JPA
-- Base de datos: MySQL 8
-- Autor: Luis Vallejos
-- Fecha: 2025-10-09
-- ==========================================================

-- Asegura la codificación y zona horaria correctas
SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- TABLA: roles
-- Almacena los roles disponibles en el sistema (ej. ROLE_ADMIN, ROLE_USER)
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: usuarios
-- Almacena la información de los usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    correo VARCHAR(50) NOT NULL UNIQUE,
    contrasenia VARCHAR(60) NOT NULL, -- Longitud para hash BCrypt
    version BIGINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: usuarios_roles (Tabla de unión para la relación Many-to-Many)
-- Vincula usuarios con sus roles
CREATE TABLE IF NOT EXISTS usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuario_roles_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_usuario_roles_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: salas
-- Almacena la información de las salas de reuniones
CREATE TABLE IF NOT EXISTS salas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(50) NOT NULL,
    version BIGINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: equipos
-- Almacena el equipamiento disponible (proyectores, laptops, etc.)
CREATE TABLE IF NOT EXISTS equipos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    descripcion VARCHAR(100),
    version BIGINT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: reservas
-- Registra las reservas de salas por parte de los usuarios
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    sala_id BIGINT NOT NULL,
    inicio DATETIME NOT NULL,
    fin DATETIME NOT NULL,
    version BIGINT DEFAULT 0,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_reserva_sala FOREIGN KEY (sala_id) REFERENCES salas(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================================
-- ÍNDICES
-- ==========================================================
-- Índice para búsquedas rápidas por sala y hora para evitar solapamientos
CREATE INDEX idx_reserva_sala_hora ON reservas (sala_id, inicio, fin);

-- Índice para listar rápidamente las reservas de un usuario
CREATE INDEX idx_reserva_usuario ON reservas (usuario_id);

-- ==========================================================
-- DATOS INICIALES (ROLES Y USUARIOS)
-- ==========================================================
-- 1. Insertar los roles base del sistema
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- 2. Insertar usuario administrador
-- Contraseña: 'admin' (encriptada con BCrypt)
INSERT INTO usuarios (id, nombre, correo, contrasenia)
VALUES (1, 'Administrador', 'admin@consultas.com', '$2a$12$Abe7a0os7seO1zxhYTOtceGPGVgP6nh8YYghUxCXHrdemNXy6AHWq')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), correo=VALUES(correo), contrasenia=VALUES(contrasenia);

-- 3. Insertar usuario de prueba
-- Contraseña: 'user' (encriptada con BCrypt)
INSERT INTO usuarios (id, nombre, correo, contrasenia)
VALUES (2, 'Usuario', 'user@consultas.com', '$2a$12$Abe7a0os7seO1zxhYTOtceGPGVgP6nh8YYghUxCXHrdemNXy6AHWq')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), correo=VALUES(correo), contrasenia=VALUES(contrasenia);

-- 4. Asignar roles a los usuarios
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1) -- admin@consultas.com -> ROLE_ADMIN
ON DUPLICATE KEY UPDATE usuario_id=VALUES(usuario_id), rol_id=VALUES(rol_id);
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 2) -- user@consultas.com -> ROLE_USER
ON DUPLICATE KEY UPDATE usuario_id=VALUES(usuario_id), rol_id=VALUES(rol_id);

-- ==========================================================
-- DATOS DE EJEMPLO ADICIONALES
-- ==========================================================
INSERT INTO salas (nombre, capacidad, ubicacion)
VALUES ('Sala Creativa', 12, 'Piso 1, Ala Norte'),
       ('Sala Enfoque', 6, 'Piso 2, Ala Sur')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

INSERT INTO equipos (nombre, tipo, estado, descripcion)
VALUES ('Proyector HD Epson', 'Proyector', 'DISPONIBLE', 'Proyector con conexión HDMI y VGA'),
       ('Pizarra Interactiva', 'Pizarra', 'MANTENIMIENTO', 'Pizarra digital táctil')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);
