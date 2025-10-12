-- Paso 1: Eliminar la llave foránea que depende del índice que vamos a borrar.
ALTER TABLE reservas DROP FOREIGN KEY fk_reserva_sala;

-- Paso 2: Ahora sí, eliminar el índice antiguo.
DROP INDEX idx_reserva_sala_hora ON reservas;

-- Paso 3: Agregar la nueva columna 'estado' con su valor por defecto.
ALTER TABLE reservas
ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'CONFIRMADA';

-- Paso 4: Crear el nuevo índice optimizado que incluye el estado.
CREATE INDEX idx_reserva_sala_hora_confirmada ON reservas (sala_id, inicio, fin, estado);

-- Paso 5: Volver a crear la llave foránea para mantener la integridad de los datos.
ALTER TABLE reservas
ADD CONSTRAINT fk_reserva_sala FOREIGN KEY (sala_id) REFERENCES salas(id)
    ON DELETE CASCADE ON UPDATE CASCADE;

