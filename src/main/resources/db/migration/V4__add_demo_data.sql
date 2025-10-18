-- ==========================================================
-- SCRIPT DE MIGRACIÓN PARA DATOS DE DEMOSTRACIÓN
-- Versión: 4
-- Autor: Asistente de Programación
-- Fecha: 2025-10-18
-- ==========================================================

-- Más usuarios de ejemplo
-- Contraseña para todos: 'user'
INSERT INTO usuarios (id, nombre, correo, contrasenia)
VALUES (3, 'Ana Torres', 'ana.torres@example.com', '$2a$12$BqbMyRKEtTBHtIMvuHA4zO/znf/wkYGsJXRWDncW4YcfO6KPfgxlG'),
       (4, 'Carlos Ruiz', 'carlos.ruiz@example.com', '$2a$12$BqbMyRKEtTBHtIMvuHA4zO/znf/wkYGsJXRWDncW4YcfO6KPfgxlG')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Asignar rol de USER a los nuevos usuarios
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (3, 2), (4, 2)
ON DUPLICATE KEY UPDATE usuario_id=VALUES(usuario_id);

-- Más salas de ejemplo
INSERT INTO salas (id, nombre, capacidad, ubicacion, activa)
VALUES (3, 'Sala de Innovación', 20, 'Piso 3, Ala Este', true),
       (4, 'Sala de Colaboración', 8, 'Piso 1, Ala Oeste', true),
       (5, 'Auditorio Menor', 50, 'Piso 5, Central', false)
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Más equipos de ejemplo
INSERT INTO equipos (id, nombre, tipo, estado, descripcion)
VALUES (3, 'Sistema de Videoconferencia Polycom', 'Electrónico', 'DISPONIBLE', 'Equipo para reuniones virtuales'),
       (4, 'Laptop de Préstamo Dell', 'Computadora', 'EN_USO', 'Laptop con software de diseño'),
       (5, 'Proyector Láser 4K', 'Proyector', 'DISPONIBLE', 'Proyector de alta definición para presentaciones')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

-- Asignar equipos a las salas
-- Sala Creativa (ID 1) tiene Pizarra y Monitor
-- Sala Enfoque (ID 2) no tiene equipos adicionales
-- Sala de Innovación (ID 3) tiene Videoconferencia y Proyector Láser
INSERT INTO salas_equipos (sala_id, equipo_id) VALUES (1, 1), (1, 2), (3, 3), (3, 5)
ON DUPLICATE KEY UPDATE sala_id=VALUES(sala_id);

-- Reservas de ejemplo
-- Asegúrate de que las fechas sean futuras para que sean relevantes
INSERT INTO reservas (usuario_id, sala_id, inicio, fin, estado)
VALUES (2, 1, '2025-10-20 10:00:00', '2025-10-20 11:30:00', 'CONFIRMADA'), 
       (3, 4, '2025-10-21 14:00:00', '2025-10-21 16:00:00', 'CONFIRMADA'),
       (4, 1, '2025-10-22 09:00:00', '2025-10-22 10:00:00', 'CANCELADA')  
ON DUPLICATE KEY UPDATE usuario_id=VALUES(usuario_id);
