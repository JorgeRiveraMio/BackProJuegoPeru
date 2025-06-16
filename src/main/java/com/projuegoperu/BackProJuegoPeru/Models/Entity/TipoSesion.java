package com.projuegoperu.BackProJuegoPeru.Models.Entity;


import com.projuegoperu.BackProJuegoPeru.Models.Enums.NombreTipoSesion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tipoSesion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoSesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", length = 100, nullable = false)
    private NombreTipoSesion nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "costo", precision = 10, scale = 2, nullable = false)
    private BigDecimal costo;

}
