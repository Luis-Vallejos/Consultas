package com.practica.consultas.repository.specification;

import com.practica.consultas.model.Equipo;
import com.practica.consultas.model.Sala;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class SalaSpecification {

    public static Specification<Sala> tieneCapacidadMinima(Integer capacidad) {
        return (root, query, criteriaBuilder)
                -> capacidad == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get("capacidad"), capacidad);
    }

    // -- ANTES --
    // public static Specification<Sala> tieneEquipo(Long equipoId) { ... }
    // -- AHORA --
    // Cambiamos el nombre y la lógica para buscar por el TIPO de equipo.
    // Usamos StringUtils.hasText para asegurarnos de que el filtro solo se aplica
    // si el tipo de equipo no es nulo ni está vacío.
    public static Specification<Sala> conTipoDeEquipo(String tipoEquipo) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(tipoEquipo)) {
                return criteriaBuilder.conjunction();
            }
            // Hacemos un join con la relación "equipos" de la sala
            Join<Sala, Equipo> equipoJoin = root.join("equipos");
            // Comparamos el campo "tipo" del equipo, ignorando mayúsculas/minúsculas
            return criteriaBuilder.equal(criteriaBuilder.lower(equipoJoin.get("tipo")), tipoEquipo.toLowerCase());
        };
    }

    public static Specification<Sala> estaActiva(Boolean activa) {
        return (root, query, criteriaBuilder)
                -> activa == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("activa"), activa);
    }
}
