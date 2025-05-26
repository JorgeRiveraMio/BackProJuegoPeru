package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoSesion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "sesion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;
    // Terapeuta (empleado que atiende)
    @ManyToOne
    @JoinColumn(name = "empleado_terapeuta_id", nullable = false)
    private Empleado terapeuta;

    // Administrador (empleado que agenda la sesión)
    @ManyToOne
    @JoinColumn(name = "empleado_admin_id", nullable = false)
    private Empleado administrador;

    // Tipo de sesión
    @ManyToOne
    @JoinColumn(name = "tipo_sesion_id", nullable = false)
    private TipoSesion tipoSesion;

    @Column(name = "fecha_sesion", nullable = false)
    private LocalDate fechaSesion;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    @Column(name = "fecha_registro", nullable = false)
    private String fechaRegistro; // puedes cambiarlo a LocalDateTime si lo prefieres

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 50)
    private EstadoSesion estado;

}
