package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Config.SecurityConfig;
import com.projuegoperu.BackProJuegoPeru.Models.DAO.UsuarioDao;
import com.projuegoperu.BackProJuegoPeru.Models.DTO.*;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/segurity")
public class AuthenticationController {

    @Autowired
    private AuthenticateService authenticate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private UsuarioService usuarioService;

//    @Autowired
//    private EmailService mailManager;

    // Un mapa temporal para guardar clientes pendientes de verificación
    private final Map<String, String> verificationCodes = new HashMap<>(); // Email -> Código

    private final Map<String, UsuarioDto> pendingClients = new HashMap<>(); // Email -> Cliente

    // Enviar código de verificación al correo
    @PostMapping("/enviarCodigo")
    public ResponseEntity<Object> enviarCodigo(@RequestBody UsuarioDto usuario) {


        if(usuarioService.obtenerUsuario(usuario.getUsername()) != null) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo ya está registrado.");
            return ResponseEntity.badRequest().body(response);
        }
        // Enviar el código al correo del cliente
        String code = authenticate.sendMessageUser(usuario.getUsername());

        // Guardar el correo y el código temporalmente
        verificationCodes.put(usuario.getUsername(), code);
        pendingClients.put(usuario.getUsername(), usuario);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Código de verificación enviado al correo.");
        return ResponseEntity.ok(response);
    }

    // Validar el código ingresado por el usuario
    @PostMapping("/validarCodigo")
    public ResponseEntity<Object> validarCodigo(@RequestBody AuthValidateCodRequest request) {
        String email = request.username();
        String code = request.code();

        // Logs para depuración, esto es solo para ver el la consola los datos (es para pruebas, eliminen si quieren xd )
        System.out.println("Email recibido: " + email);
        System.out.println("Código recibido: " + code);
        System.out.println("Código esperado: " + verificationCodes.get(email));

        // Verificar si el código ingresado coincide con el que fue enviado
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            // Obtener el cliente temporal
            UsuarioDto cliente = pendingClients.get(email);
            // Guardar el cliente en la base de datos
            String passwordEncriptado = passwordEncoder.encode(cliente.getPassword());
            cliente.setPassword(passwordEncriptado);
            this.userDetailService.createUser(cliente);
            // Eliminar el cliente y el código del mapa temporal
            verificationCodes.remove(email);
            pendingClients.remove(email);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Código validado. Usuario registrado exitosamente.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Código de verificación incorrecto.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        UserDetails userDetails = this.userDetailService.loadUserByUsername(principal.getName());//toma el nombre de usuario en nuestro caso el correo
        if (userDetails instanceof UsuarioDto) {// si el usuario es un cliente, pues devuelve su informacion
            return ResponseEntity.ok((UsuarioDto) userDetails);
        }
        return ResponseEntity.badRequest().body("Usuario no encontrado");//si no se encuentra entonces no hay usuario
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
