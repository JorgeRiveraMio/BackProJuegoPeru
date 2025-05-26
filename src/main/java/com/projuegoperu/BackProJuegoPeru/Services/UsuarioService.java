package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRespository usuarioRespository;

    public List<Usuario> Listar() {
        return usuarioRespository.findAll();
    }

    public Usuario Guardar(Usuario u) {
        return usuarioRespository.save(u);
    }

    public Usuario ObtenerPorId(Integer id) {
        return usuarioRespository.findById(id).orElse(null);
    }

    public Optional<Usuario> obtenerUsuario(String username) {
        return usuarioRespository.findByUsername(username);
    }

    public Usuario actualizarUsuario(Usuario usuario) {
        Usuario usuarioExistente = usuarioRespository.findById(usuario.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuario.getIdUsuario()));
        
        if (usuario instanceof Tutor) {
            Tutor tutorExistente = (Tutor) usuarioExistente;
            tutorExistente.setDireccion(((Tutor) usuario).getDireccion());
            tutorExistente.setTelefono(((Tutor) usuario).getTelefono());
        }
        return usuarioRespository.save(usuarioExistente);
    }
    

}
