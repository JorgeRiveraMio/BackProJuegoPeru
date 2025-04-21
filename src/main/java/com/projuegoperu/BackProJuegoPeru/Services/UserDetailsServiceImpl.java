package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.Entity.Rol;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
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

        usuario.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);  // Cambio aquí para que use el email

    }
    public AuthResponse createUser(UsuarioDto createRoleRequest) {


        String username = createRoleRequest.getUsername();
        System.out.println("Contraseña de registro: " +  createRoleRequest.getPassword());
        String password =passwordEncoder.encode( createRoleRequest.getPassword());
        System.out.println("Contraseña de registro hash: " +  password);
        List<String> rolesRequest = createRoleRequest.getRoles().stream()
                .map(rol -> rol) // Suponiendo que 'getNombre()' te da el nombre del rol
                .collect(Collectors.toList());


        Set<Rol> roleEntityList = rolRepository.findByNameIn(rolesRequest).stream().collect(Collectors.toSet());

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

            Usuario userEntity = new Usuario();
//            userEntity.setIdPersona(1);
            userEntity.setName(createRoleRequest.getName());
            userEntity.setLastname(createRoleRequest.getLastname());
            userEntity.setDni(createRoleRequest.getDni());
            userEntity.setUsername(username);
            userEntity.setPassword(password);
            userEntity.setCreationDate(LocalDateTime.now());
//            userEntity.setTipoUsuario(createRoleRequest.getTipoUsuario().toString());
        userEntity.setTipoUsuario(createRoleRequest.getTipoUsuario()); // directo, sin usar valueOf


        userEntity.setRoles(roleEntityList);

        Usuario userSaved = usuarioRespository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));


        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        String accessToken = jwtUtils.createToken(authentication);
        List<String> rolesList = userSaved.getRoles().stream()
                .map(Rol::getName)
                .toList();
        AuthResponse authResponse = new AuthResponse(username, "User created successfully",rolesList, accessToken, true);
        return authResponse;
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

        List<String> rolesList = usuario.getRoles()
                .stream()
                .map(Rol::getName)
                .collect(Collectors.toList());

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
