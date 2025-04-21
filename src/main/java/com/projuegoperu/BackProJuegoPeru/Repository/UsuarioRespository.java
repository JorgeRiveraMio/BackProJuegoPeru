package com.projuegoperu.BackProJuegoPeru.Repository;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
//import com.projuegoperu.BackProJuegoPeru.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
//import com.projuegoperu.BackProJuegoPeru.Models.Persona;

@Repository
public interface UsuarioRespository extends JpaRepository<Usuario, Integer> {
//    Usuario findByUsername(String username);
Optional<Usuario> findByUsername(String username);
}
