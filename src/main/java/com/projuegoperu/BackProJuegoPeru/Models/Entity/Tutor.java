package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoTutor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tutor")
@PrimaryKeyJoinColumn(name = "idUsuario") 
public class Tutor extends Usuario {

    @Column(name = "direccion")
    private String direccion; 

    @Column(name = "telefono")
    private String telefono; 
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTutor estadoTutor; //ACTIVO, INACTIVO

}
