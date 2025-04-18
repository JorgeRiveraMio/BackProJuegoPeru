package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.UsuarioDao;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRespository usuarioRespository;

    public List<UsuarioDao> Listar() {
        return usuarioRespository.findAll();
    }

    public UsuarioDao Guardar(UsuarioDao u) {
        return usuarioRespository.save(u);
    }

    public UsuarioDao Obtener(Integer id) {
        return usuarioRespository.findById(id).orElse(null);
    }

    public Optional<UsuarioDao> obtenerUsuario(String username) {
        return usuarioRespository.findByUsername(username);
    }

}
