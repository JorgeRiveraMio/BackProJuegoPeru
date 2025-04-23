package com.projuegoperu.BackProJuegoPeru.Models.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rol")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    // Constructor que solo recibe el name
    public Rol(String name) {
        this.name = name;
    }

    //  tener acceso a los usuarios con este rol
    // @OneToMany(mappedBy = "rol")
    // private List<Usuario> usuarios;
}
