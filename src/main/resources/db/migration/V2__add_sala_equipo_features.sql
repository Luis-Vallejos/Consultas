-- Agrega la columna 'activa' a la tabla 'salas' para poder habilitar o deshabilitar salas.
ALTER TABLE salas ADD COLUMN activa BOOLEAN NOT NULL DEFAULT TRUE;

-- Asegura que el nombre del equipo y sala sean únicos.
-- La columna 'descripcion' ya fue creada por Hibernate, por lo que se omite su adición aquí para evitar el error.
ALTER TABLE equipos ADD CONSTRAINT uq_equipo_nombre UNIQUE (nombre);
ALTER TABLE salas ADD CONSTRAINT uq_sala_nombre UNIQUE (nombre);


-- Crea la tabla de unión para la relación muchos a muchos entre salas y equipos.
CREATE TABLE salas_equipos (
    sala_id BIGINT NOT NULL,
    equipo_id BIGINT NOT NULL,
    PRIMARY KEY (sala_id, equipo_id),
    CONSTRAINT fk_sala_equipos_sala FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE CASCADE,
    CONSTRAINT fk_sala_equipos_equipo FOREIGN KEY (equipo_id) REFERENCES equipos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Datos de ejemplo para la nueva funcionalidad
UPDATE salas SET activa = TRUE;

-- Inserta los equipos de ejemplo solo si no existen para evitar errores en ejecuciones repetidas.
INSERT INTO equipos (nombre, tipo, estado, descripcion)
VALUES ('Pizarra Blanca', 'Mobiliario', 'DISPONIBLE', 'Pizarra de 2x1.5 metros'),
       ('Monitor 4K', 'Electrónico', 'DISPONIBLE', 'Monitor Samsung 32 pulgadas 4K')
ON DUPLICATE KEY UPDATE nombre=nombre; -- Esta línea evita el error si los registros ya existen.

