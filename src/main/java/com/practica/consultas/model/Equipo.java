package com.practica.consultas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Luis
 */
@Entity
@Table(name = "equipos", schema = "consultas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "salas")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Equipo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    @Column(name = "tipo", length = 50, nullable = false)
    private String tipo;

    @Column(name = "estado", length = 50, nullable = false)
    private String estado;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    // Conexiones
    @Builder.Default
    @JsonIgnore // Para evitar recursión infinita al serializar
    @ManyToMany(mappedBy = "equipos")
    private Set<Sala> salas = new HashSet<>();

    @Version
    @Column(name = "Version")
    private Long version;

}
