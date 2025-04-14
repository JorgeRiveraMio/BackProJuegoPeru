package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DAO.UsuarioDao;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthCreateUserRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthLoginRequest;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.AuthResponse;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.UsuarioDto;
import com.projuegoperu.BackProJuegoPeru.Repository.RolRepository;
import com.projuegoperu.BackProJuegoPeru.Services.AuthenticateService;
import com.projuegoperu.BackProJuegoPeru.Services.EmailService;
import com.projuegoperu.BackProJuegoPeru.Services.UserDetailsServiceImpl;
import com.projuegoperu.BackProJuegoPeru.Services.UsuarioService;
import com.projuegoperu.BackProJuegoPeru.Utils.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/segurity")
public class AuthenticationController {

    @Autowired
    private AuthenticateService authenticate;

    @Autowired
    private UserDetailsServiceImpl userDetailService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EmailService mailManager;

    // Un mapa temporal para guardar clientes pendientes de verificación
    private final Map<String, String> verificationCodes = new HashMap<>(); // Email -> Código

    private final Map<String, UsuarioDto> pendingClients = new HashMap<>(); // Email -> Cliente

    // Enviar código de verificación al correo
    @PostMapping("/enviarCodigo")
    public ResponseEntity<Object> enviarCodigo(@RequestBody UsuarioDto cliente) {
        // Validar si el correo ya esta registrado
        if(usuarioService.obtenerUsuario(cliente.getUsername()) != null) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo ya está registrado.");
            return ResponseEntity.badRequest().body(response);

        }
        // Enviar el código al correo del cliente
        String code = authenticate.sendMessageUser(cliente.getUsername());

        // Guardar el correo y el código temporalmente
        verificationCodes.put(cliente.getUsername(), code);
        pendingClients.put(cliente.getUsername(), cliente);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Código de verificación enviado al correo.");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UsuarioDto userRequest){
        return new ResponseEntity<>(this.userDetailService.createUser(userRequest), HttpStatus.CREATED);
    }

    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }
}
