package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoEmpleado;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoTutor;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.TutorDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.EmpleadoDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
import com.projuegoperu.BackProJuegoPeru.Repository.PacienteRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.UsuarioRespository;
import com.projuegoperu.BackProJuegoPeru.Utils.JwtUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    private PacienteRepository pacienteRepository;

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

    @Transactional
    public AuthResponse createUser(UsuarioDto usuarioDto) {
        // Extraer username y codificar la contraseña
        String username = usuarioDto.getUsername();
        String password = passwordEncoder.encode(usuarioDto.getPassword());
        System.out.println("Creando usuario: " + usuarioDto.getUsername());

        // Verificar si ya existe un usuario con el mismo username
        Optional<Usuario> existingUser = usuarioRespository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El username ya está registrado.");
        }

        if (pacienteRepository.existsByDni(usuarioDto.getDni()) || usuarioRespository.existsByDni(usuarioDto.getDni())) {
            throw new RuntimeException("El DNI ya está registrado en otra entidad");
        }


        // Obtener el nombre del rol directamente desde el DTO
        int idRol = usuarioDto.getIdRol();
        System.out.println("ID del rol recibido: " + idRol);
        // Buscar el rol existente en la base de datos
        Rol rolAsignado = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe.Id : " + idRol));

        // Log para depurar el rol asignado
        System.out.println("Rol asignado: " + rolAsignado.getName());
        Usuario userEntity;
        switch (rolAsignado.getName()) {
            case "ROLE_TUTOR":
                Tutor tutor = new Tutor();
                tutor.setName(usuarioDto.getName());
                tutor.setLastname(usuarioDto.getLastname());
                tutor.setDni(usuarioDto.getDni());
                tutor.setUsername(usuarioDto.getUsername());
                tutor.setPassword(usuarioDto.getPassword());
                tutor.setCreationDate(usuarioDto.getCreationDate());
                // Estos pueden llegar como null, está bien
                tutor.setDireccion(null);
                tutor.setTelefono(null);
                tutor.setEstadoTutor(EstadoTutor.ACTIVO);
                userEntity = tutor;
                break;

            default:
                userEntity = new Usuario(); // Usuario genérico, por si acaso
                break;
        }

        // Asignar atributos comunes
        userEntity.setName(usuarioDto.getName());
        userEntity.setLastname(usuarioDto.getLastname());
        userEntity.setDni(usuarioDto.getDni());
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setCreationDate(LocalDateTime.now());
        userEntity.setRol(rolAsignado);

        // Log para depurar el usuario que se va a guardar
        System.out.println("Usuario a guardar: " + userEntity);

        // Guardar el usuario en la base de datos
        Usuario userSaved = usuarioRespository.save(userEntity);
        System.out.println("Usuario guardado: " + userSaved.getIdUsuario());

        // Autenticación manual + token
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userSaved.getRol().getName())
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Crear el token JWT
        String accessToken = jwtUtils.createToken(authentication);
        List<String> rolesList = List.of(userSaved.getRol().getName());

        // Respuesta de éxito
        return new AuthResponse(username, "Usuario creado exitosamente", rolesList, accessToken, true);
    }
    @Transactional
    public AuthResponse createEmpleado(EmpleadoDto empleadoDto) {
        // Extraer username y codificar la contraseña
        String username = empleadoDto.getUsername();
        String password = passwordEncoder.encode(empleadoDto.getPassword());
        System.out.println("Creando usuario: " + empleadoDto.getUsername());

        // Verificar si ya existe un usuario con el mismo username
        Optional<Usuario> existingUser = usuarioRespository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El username ya está registrado.");
        }
        if (pacienteRepository.existsByDni(empleadoDto.getDni()) || usuarioRespository.existsByDni(empleadoDto.getDni())) {
            throw new RuntimeException("El DNI ya está registrado en otra entidad");
        }
        // Obtener el nombre del rol directamente desde el DTO
        int idRol = empleadoDto.getIdRol();
        System.out.println("ID del rol recibido: " + idRol);
        // Buscar el rol existente en la base de datos
        Rol rolAsignado = rolRepository.findById(idRol)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe.Id : " + idRol));

        // Log para depurar el rol asignado
        System.out.println("Rol asignado: " + rolAsignado.getName());
        Usuario userEntity;
        switch (rolAsignado.getName()) {
            case "ROLE_ADMIN":
            case "ROLE_TERAPEUTA":

                Empleado empleado = new Empleado();
                empleado.setLastname(empleadoDto.getLastname());
                empleado.setDni(empleadoDto.getDni());
                empleado.setUsername(empleadoDto.getUsername());
                empleado.setPassword(empleadoDto.getPassword());
                empleado.setCreationDate(empleadoDto.getCreationDate());
                empleado.setEspecialidad(empleadoDto.getEspecialidad());
                empleado.setEstadoEmpleado(EstadoEmpleado.ACTIVO);
                userEntity = empleado;
                break;

            default:
                userEntity = new Usuario(); // Usuario genérico, por si acaso
                break;
        }

        // Asignar atributos comunes
        userEntity.setName(empleadoDto.getName());
        userEntity.setLastname(empleadoDto.getLastname());
        userEntity.setDni(empleadoDto.getDni());
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setCreationDate(LocalDateTime.now());
        userEntity.setRol(rolAsignado);

        // Log para depurar el usuario que se va a guardar
        System.out.println("Empleado a guardar: " + userEntity);

        // Guardar el usuario en la base de datos
        Usuario userSaved = usuarioRespository.save(userEntity);
        System.out.println("Empleado guardado: " + userSaved.getIdUsuario());

        // Autenticación manual + token
        List<SimpleGrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(userSaved.getRol().getName())
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Crear el token JWT
        String accessToken = jwtUtils.createToken(authentication);
        List<String> rolesList = List.of(userSaved.getRol().getName());

        // Respuesta de éxito
        return new AuthResponse(username, "Usuario creado exitosamente", rolesList, accessToken, true);
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
