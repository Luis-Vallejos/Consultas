package com.practica.consultas.repository.specification;

import com.practica.consultas.model.Equipo;
import com.practica.consultas.model.Sala;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Luis
 */
public class SalaSpecification {

    public static Specification<Sala> tieneCapacidadMinima(Integer capacidad) {
        return (root, query, criteriaBuilder)
                -> capacidad == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.greaterThanOrEqualTo(root.get("capacidad"), capacidad);
    }

    public static Specification<Sala> tieneEquipo(Long equipoId) {
        return (root, query, criteriaBuilder) -> {
            if (equipoId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Sala, Equipo> equipoJoin = root.join("equipos");
            return criteriaBuilder.equal(equipoJoin.get("id"), equipoId);
        };
    }

    public static Specification<Sala> estaActiva(Boolean activa) {
        return (root, query, criteriaBuilder)
                -> activa == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("activa"), activa);
    }
}
