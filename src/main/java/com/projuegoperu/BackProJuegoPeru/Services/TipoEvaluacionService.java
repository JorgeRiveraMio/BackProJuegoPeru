package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoEvaluacion;
import com.projuegoperu.BackProJuegoPeru.Repository.TipoEvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoEvaluacionService {

    @Autowired
    private TipoEvaluacionRepository tipoEvaluacionRepository;

    public List<TipoEvaluacion> listarTodos() {
        return tipoEvaluacionRepository.findAll();
    }

    public Optional<TipoEvaluacion> obtenerPorId(Integer id) {
        return tipoEvaluacionRepository.findById(id);
    }

    public TipoEvaluacion guardar(TipoEvaluacion tipoEvaluacion) {
        return tipoEvaluacionRepository.save(tipoEvaluacion);
    }

    public void eliminar(Integer id) {
        tipoEvaluacionRepository.deleteById(id);
    }
}
