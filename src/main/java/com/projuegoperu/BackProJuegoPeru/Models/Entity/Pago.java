package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoPago;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.MetodoPago;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Relación con la sesión
    @ManyToOne
    @JoinColumn(name = "sesion_id", nullable = false)
    private Sesion sesion;

    // Relación con el tutor (puede ser una entidad Tutor o Paciente responsable)
    @ManyToOne
    @JoinColumn(name = "tutor_id", nullable = false)
    private Tutor tutor;

    @Column(name = "monto", precision = 10, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;
    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", length = 50, nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50, nullable = false)
    private EstadoPago estado;


    @Column(name = "referencia_pago", length = 100)
    private String referenciaPago;
}
