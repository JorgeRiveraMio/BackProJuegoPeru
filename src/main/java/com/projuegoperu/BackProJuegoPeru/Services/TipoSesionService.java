package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Sesion;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.TipoSesion;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.NombreTipoSesion;
import com.projuegoperu.BackProJuegoPeru.Repository.SesionRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.TipoSesionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    @PostConstruct
    public void init() {
        // Solo insertar si no hay datos
        if (sesionRepository.count() == 0) {
            sesionRepository.saveAll(List.of(
                    new TipoSesion(NombreTipoSesion.TERAPIA_OCUPACIONAL,
                            "Tecnólogo médico en el área de terapia ocupacional - 50 minutos",
                            new BigDecimal("110.00")),

                    new TipoSesion(NombreTipoSesion.TERAPIA_LENGUAJE,
                            "Tecnólogo médico en el área de lenguaje - 50 minutos",
                            new BigDecimal("110.00")),

                    new TipoSesion(NombreTipoSesion.TERAPIA_CONDUCTA,
                            "Psicólogos colegiados - 50 minutos",
                            new BigDecimal("110.00")),

                    new TipoSesion(NombreTipoSesion.TERAPIA_APRENDIZAJE,
                            "Psicopedagoga - 50 minutos",
                            new BigDecimal("110.00")),

                    new TipoSesion(NombreTipoSesion.TERAPIA_FISICA,
                            "Tecnólogo médico en el área física - 50 minutos",
                            new BigDecimal("110.00"))
            ));
        }
    }
    }
