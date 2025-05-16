package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.ClienteDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.EmpleadoDto;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
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

        // Obtener el nombre del rol directamente desde el DTO
        String rolNombre = usuarioDto.getRol().getName();

        // Buscar el rol existente en la base de datos
        Rol rolAsignado = rolRepository.findByName(rolNombre)
                .orElseThrow(() -> new IllegalArgumentException("El rol especificado no existe."));

        // Log para depurar el rol asignado
        System.out.println("Rol asignado: " + rolAsignado.getName());

        Usuario userEntity;

        switch (rolNombre) {
            case "ROLE_ADMIN":
            case "ROLE_TERAPEUTA":
                if (!(usuarioDto instanceof EmpleadoDto empleadoDto)) {
                    throw new IllegalArgumentException("Se esperaba un EmpleadoDto para el rol " + rolNombre);
                }
                Empleado empleado = new Empleado();
                empleado.setEspecialidad(empleadoDto.getEspecialidad());
                empleado.setEstadoEmpleado(empleadoDto.getEstadoEmpleado());
                empleado.setTipoUsuario(TipoUsuario.EMPLEADO); // <- IMPORTANTE
                userEntity = empleado;
                break;

            case "ROLE_CLIENTE":
                if (!(usuarioDto instanceof ClienteDto clienteDto)) {
                    throw new IllegalArgumentException("Se esperaba un ClienteDto para el rol " + rolNombre);
                }
                Tutor tutor = new Tutor();
                tutor.setDireccion(clienteDto.getDireccion());
                tutor.setTelefono(clienteDto.getTelefono());
                tutor.setEstadoCliente(clienteDto.getEstado());
                tutor.setTipoUsuario(TipoUsuario.CLIENTE); // <- IMPORTANTE
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
