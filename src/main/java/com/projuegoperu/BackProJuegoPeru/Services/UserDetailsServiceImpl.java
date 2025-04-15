package com.projuegoperu.BackProJuegoPeru.Services;


import com.projuegoperu.BackProJuegoPeru.Models.DAO.RolDao;
import com.projuegoperu.BackProJuegoPeru.Models.DAO.UsuarioDao;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthCreateUserRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
import com.projuegoperu.BackProJuegoPeru.Models.Rol;
//import com.projuegoperu.BackProJuegoPeru.Models.Usuario;
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
//    @Autowired
//    private JwtUtils jwtUtils;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private RolRepository rolRepository;
//
//
//    @Autowired
//    private UsuarioRespository usuarioRespository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if (username == null || username.isEmpty()) {
//            throw new UsernameNotFoundException("Username cannot be null or empty");
//        }
//        Usuario usuario = usuarioRespository.findByUsername(username);
//        if (usuario == null) {
//            throw new UsernameNotFoundException("Usuario no encontrado");
//        }
//        List<SimpleGrantedAuthority>authorities = new ArrayList<>();
//        usuario.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});
//
//        return new User(usuario.getUsername(), usuario.getPassword(), authorities);  // Cambio aquí para que use el email
//
//    }
//    public AuthResponse createUser(AuthCreateUserRequest createRoleRequest) {
//
//        String username = createRoleRequest.username();
//        String password = createRoleRequest.password();
//        List<String> rolesRequest = createRoleRequest.roleRequest().roleListName();
//
//        Set<Rol> roleEntityList = rolRepository.findByNameIn(rolesRequest).stream().collect(Collectors.toSet());
//
//        if (roleEntityList.isEmpty()) {
//            throw new IllegalArgumentException("The roles specified does not exist.");
//        }
//
//        Usuario userEntity = new Usuario(
//                0,
//                createRoleRequest.name(),
//                createRoleRequest.lastname(),
//                createRoleRequest.dni(),
//                username,
//                passwordEncoder.encode(password),
//                LocalDateTime.now(),
//                "cliente",
//                roleEntityList
//        );
//
//        Usuario userSaved = usuarioRespository.save(userEntity);
//
//        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//         userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));
//
//        //userSaved.getRoles().stream().flatMap(role -> role.getPermissionList().stream()).forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));
//
//        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
//
//        String accessToken = jwtUtils.createToken(authentication);
//        List<String> rolesList = userSaved.getRoles().stream()
//                .map(Rol::getName)
//                .toList();
//        AuthResponse authResponse = new AuthResponse(username, "User created successfully",rolesList, accessToken, true);
//        return authResponse;
//    }
//    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {
//
//        String username = authLoginRequest.username();
//        String password = authLoginRequest.password();
//
//        Authentication authentication = this.authenticate(username, password);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String accessToken = jwtUtils.createToken(authentication);
//
//        Usuario user = usuarioRespository.findByUsername(username);
//        List<String>rolesList =user.getRoles().stream().map(Rol::getName).collect(Collectors.toList());
//        AuthResponse authResponse = new AuthResponse(username, "User loged succesfully",rolesList, accessToken, true);
//        return authResponse;
//    }
//    public Authentication authenticate(String username, String password) {
//        UserDetails userDetails = this.loadUserByUsername(username);
//
//        if (userDetails == null) {
//            throw new BadCredentialsException(String.format("Invalid username or password"));
//        }
//
//        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
//            throw new BadCredentialsException("Incorrect Password");
//        }
//
//        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
//    }

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
        UsuarioDao usuario = usuarioRespository.findByUsername(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        List<SimpleGrantedAuthority>authorities = new ArrayList<>();
        usuario.getRoles().forEach(role -> {authorities.add(new SimpleGrantedAuthority(role.getName()));});

        return new User(usuario.getUsername(), usuario.getPassword(), authorities);  // Cambio aquí para que use el email

    }
    public AuthResponse createUser(UsuarioDto createRoleRequest) {


        String username = createRoleRequest.getUsername();
        String password =passwordEncoder.encode( createRoleRequest.getPassword());
        List<String> rolesRequest = createRoleRequest.getRoles().stream()
                .map(rol -> rol) // Suponiendo que 'getNombre()' te da el nombre del rol
                .collect(Collectors.toList());


        Set<RolDao> roleEntityList = rolRepository.findByNameIn(rolesRequest).stream().collect(Collectors.toSet());

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }

            UsuarioDao userEntity = new UsuarioDao();
            userEntity.setIdPersona(1);
            userEntity.setName(createRoleRequest.getName());
            userEntity.setLastname(createRoleRequest.getLastname());
            userEntity.setDni(createRoleRequest.getDni());
            userEntity.setUsername(username);
            userEntity.setPassword(password);
            userEntity.setCreationDate(LocalDateTime.now());
            userEntity.setTipoUsuario(createRoleRequest.getTipoUsuario().toString());
            userEntity.setRoles(roleEntityList);

        UsuarioDao userSaved = usuarioRespository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();

        userSaved.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getName()))));


        SecurityContext securityContextHolder = SecurityContextHolder.getContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);

        String accessToken = jwtUtils.createToken(authentication);
        List<String> rolesList = userSaved.getRoles().stream()
                .map(RolDao::getName)
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

        UsuarioDao user = usuarioRespository.findByUsername(username);
        List<String>rolesList =user.getRoles().stream().map(RolDao::getName).collect(Collectors.toList());
        AuthResponse authResponse = new AuthResponse(username, "User loged succesfully",rolesList, accessToken, true);
        return authResponse;
    }
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException(String.format("Invalid username or password"));
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }

        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }



}
