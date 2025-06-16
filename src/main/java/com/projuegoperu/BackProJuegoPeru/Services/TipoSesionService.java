package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoSesion;
import com.projuegoperu.BackProJuegoPeru.Repository.SesionRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.TipoSesionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoSesionService {
    @Autowired
    private TipoSesionRepository sesionRepository;

    public List<TipoSesion> listarTodos() {
        return sesionRepository.findAll();
    }

    public Optional<TipoSesion> obtenerPorId(Integer id) {
        return sesionRepository.findById(id);
    }

    public TipoSesion guardar(TipoSesion sesion) {
        return sesionRepository.save(sesion);
    }

    public TipoSesion actualizar(TipoSesion sesion) {
        return sesionRepository.save(sesion);
    }

    public void eliminar(Integer id) {
        sesionRepository.deleteById(id);
    }
}
