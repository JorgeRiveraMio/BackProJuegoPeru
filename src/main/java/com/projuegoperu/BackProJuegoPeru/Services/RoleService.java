package com.projuegoperu.BackProJuegoPeru.Services;

import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RolRepository rolRepository;

    @PostConstruct
    public void init() {
        // Lista de roles que quieres verificar
        List<String> roleNames = Arrays.asList("ROLE_CLIENTE", "ROLE_ADMIN", "ROLE_TERAPEUTA");
        
        // Buscar si alguno de estos roles ya existe
        List<Rol> existingRoles = rolRepository.findByNameIn(roleNames);
        
        // Convertir la lista de roles existentes a un set para una búsqueda rápida
        List<String> existingRoleNames = existingRoles.stream()
                                                    .map(Rol::getName)
                                                    .toList();

        // Agregar los roles que no existan en la base de datos
        for (String roleName : roleNames) {
            if (!existingRoleNames.contains(roleName)) {
                // Si el rol no existe, lo creamos
                rolRepository.save(new Rol(roleName));
            }
        }
    }
}
