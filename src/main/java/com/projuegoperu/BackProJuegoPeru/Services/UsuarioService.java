package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.DAO.UsuarioDao;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public UsuarioDao obtenerUsuario(String username) {
        return usuarioRespository.findByUsername(username);
    }

}
