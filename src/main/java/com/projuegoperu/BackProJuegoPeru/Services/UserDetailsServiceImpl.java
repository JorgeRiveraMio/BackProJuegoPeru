package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.EmpleadoDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
//import com.projuegoperu.BackProJuegoPeru.Models.Rol;
//import com.projuegoperu.BackProJuegoPeru.Models.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import com.projuegoperu.BackProJuegoPeru.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolRepository;


    @Autowired
    private UsuarioRespository usuarioRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
        Optional<Usuario> usuarioDaoOptional = usuarioRespository.findByUsername(username);
        if (usuarioDaoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        Usuario usuario = usuarioDaoOptional.get();
        List<SimpleGrantedAuthority>authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(usuario.getRol().getName()));

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);  // Cambio aquí para que use el email

    }
    public AuthResponse createUser(UsuarioDto createRoleRequest) {
        // Verificar si es un empleado o usuario común
        String username = createRoleRequest.getUsername();
        System.out.println("Contraseña de registro: " + createRoleRequest.getPassword());
        String password = passwordEncoder.encode(createRoleRequest.getPassword());
        System.out.println("Contraseña de registro hash: " + password);

        List<String> rolesRequest = createRoleRequest.getRol().stream()
        .map(rol -> rol.getName()) // Obtener el nombre del rol, suponiendo que RolDto tiene un método getName()
        .collect(Collectors.toList());

        Set<Rol> roleEntityList = rolRepository.findByNameIn(rolesRequest).stream().collect(Collectors.toSet());

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified do not exist.");
        }

        Usuario userEntity;

        if (createRoleRequest.getTipoUsuario() == TipoUsuario.EMPLEADO) {
            // Verificar si es un EmpleadoDto
            if (!(createRoleRequest instanceof EmpleadoDto)) {
                throw new IllegalArgumentException("Se esperaba un EmpleadoDto");
            }

            EmpleadoDto empleadoDto = (EmpleadoDto) createRoleRequest;
            com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado empleado = new com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado();
            empleado.setTipoEmpleado(empleadoDto.getTipoEmpleado()); // Tipo de empleado (ADMIN, TERAPEUTA)
            empleado.setEspecialidad(empleadoDto.getEspecialidad()); // Opcional, si es terapeuta
            empleado.setEstadoEmpleado(empleadoDto.getEstadoEmpleado()); // Estado del empleado, si es necesario

            userEntity = empleado;
        } else {
            // Si no es un empleado, crear un usuario común
            userEntity = new Usuario();
        }

        // Asignar los valores comunes a ambos tipos
        userEntity.setName(createRoleRequest.getName());
        userEntity.setLastname(createRoleRequest.getLastname());
        userEntity.setDni(createRoleRequest.getDni());
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setCreationDate(LocalDateTime.now());
        userEntity.setTipoUsuario(createRoleRequest.getTipoUsuario());
        userEntity.setRol(roleEntityList.iterator().next());

        // Guardar el usuario o empleado en la base de datos
        Usuario userSaved = usuarioRespository.save(userEntity);

        // Autenticación y token
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + userSaved.getRol().getName()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);
        List<String> rolesList = List.of(userSaved.getRol().getName());

        return new AuthResponse(username, "User created successfully", rolesList, accessToken, true);
    }

    
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        Optional<Usuario> usuarioDaoOptional = usuarioRespository.findByUsername(username);
        if (usuarioDaoOptional.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        Usuario usuario = usuarioDaoOptional.get();

        List<String> rolesList = List.of(usuario.getRol().getName());

        return new AuthResponse(username, "User logged successfully", rolesList, accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        // Loguear las contraseñas antes de la comparación
        System.out.println("Contraseña proporcionada: " + password);
        System.out.println("Contraseña almacenada (cifrada): " + userDetails.getPassword());

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }



}
