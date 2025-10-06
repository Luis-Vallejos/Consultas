-- ==========================================================
-- SCRIPT DE MIGRACIÓN INICIAL - PROYECTO "CONSULTAS"
-- Base de datos: MySQL 8
-- Autor: Luis Vallejos
-- Fecha: 2025-10-06
-- ==========================================================

-- Ajustes generales
SET NAMES utf8mb4;
SET time_zone = '+00:00';

-- TABLA: usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    rol VARCHAR(50) DEFAULT 'USER'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: salas
CREATE TABLE IF NOT EXISTS salas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    capacidad INT NOT NULL,
    ubicacion VARCHAR(150)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: equipos
CREATE TABLE IF NOT EXISTS equipos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipo VARCHAR(100),
    estado VARCHAR(50) DEFAULT 'DISPONIBLE'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- TABLA: reservas
CREATE TABLE IF NOT EXISTS reservas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    sala_id BIGINT NOT NULL,
    inicio DATETIME NOT NULL,
    fin DATETIME NOT NULL,
    CONSTRAINT fk_reserva_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_reserva_sala FOREIGN KEY (sala_id) REFERENCES salas(id)
        ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================================
-- ÍNDICES
-- ==========================================================
-- Índice para búsquedas rápidas por sala y hora (detección de solapes)
CREATE INDEX idx_reserva_sala_hora ON reservas (sala_id, inicio, fin);

-- Índice útil para listar reservas por usuario
CREATE INDEX idx_reserva_usuario ON reservas (usuario_id);

-- ==========================================================
-- DATOS DE EJEMPLO (opcional)
-- ==========================================================
INSERT INTO usuarios (nombre, correo, rol)
VALUES ('Admin Principal', 'admin@consultas.com', 'ADMIN');

INSERT INTO salas (nombre, capacidad, ubicacion)
VALUES ('Sala Reunión A', 10, 'Piso 1'),
       ('Sala Reunión B', 8, 'Piso 2');

INSERT INTO equipos (nombre, tipo, estado)
VALUES ('Proyector Epson', 'Proyector', 'DISPONIBLE'),
       ('Laptop HP', 'Computadora', 'DISPONIBLE');
