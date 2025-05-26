package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class RoleService {

    @Autowired
    private RolRepository rolRepository;

    @PostConstruct
    public void init() {
        // Lista de roles que quieres verificar
        List<String> roleNames = Arrays.asList("ROLE_TUTOR", "ROLE_ADMIN", "ROLE_TERAPEUTA");

        // Verificar si los roles existen uno a uno
        for (String roleName : roleNames) {
            Optional<Rol> existingRole = rolRepository.findByName(roleName);
            
            // Si el rol no existe, lo creamos
            if (existingRole.isEmpty()) {
                // Si el rol no existe, lo creamos
                rolRepository.save(new Rol(roleName));
            }
        }
    }

}
