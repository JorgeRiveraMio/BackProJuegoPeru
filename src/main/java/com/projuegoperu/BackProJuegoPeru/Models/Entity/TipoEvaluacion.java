package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "tipo_evaluacion")
@Data
@Getter
@Setter
public class TipoEvaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private Double costo;

    @Column(name = "sesiones_presenciales")
    private int sesionesPresenciales;

    @Column(name = "sesiones_virtuales")
    private int sesionesVirtuales;
}
