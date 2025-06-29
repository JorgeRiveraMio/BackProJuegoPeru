package com.projuegoperu.BackProJuegoPeru.Controllador;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.*;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.PasswordResetToken;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Repository.EmpleadoRepository;
import com.projuegoperu.BackProJuegoPeru.Repository.PasswordResetTokenRepository;
import com.projuegoperu.BackProJuegoPeru.Services.AuthenticateService;
import com.projuegoperu.BackProJuegoPeru.Services.EmailService;
import com.projuegoperu.BackProJuegoPeru.Services.UserDetailsServiceImpl;
import com.projuegoperu.BackProJuegoPeru.Services.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/security")
public class TutorController {
    @Autowired
    private AuthenticateService authenticate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private EmailService mailManager;

    // Un mapa temporal para guardar clientes pendientes de verificación
    private final Map<String, String> verificationCodes = new HashMap<>(); // Email -> Código

    private final Map<String, UsuarioDto> pendingClients = new HashMap<>(); // Email -> Cliente

    @PostMapping("/enviarCodigo")
    public ResponseEntity<Object> enviarCodigo(@RequestBody UsuarioDto usuario) {

        // Validar que el correo no esté vacío
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo electrónico no puede estar vacío."));
        }

        // Validar formato de correo electrónico
        if (!usuario.getUsername().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo electrónico no tiene un formato válido."));
        }

        // Verificar si el correo ya está registrado
        Optional<Usuario> usuarioDaoOptional = usuarioService.obtenerUsuario(usuario.getUsername());
        if (usuarioDaoOptional.isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo ya está registrado."));
        }

        // Enviar el código de verificación al correo
        String code = authenticate.sendMessageUser(usuario.getUsername());

        // Guardar temporalmente
        verificationCodes.put(usuario.getUsername(), code);
        pendingClients.put(usuario.getUsername(), usuario);  // Ya no se necesita diferenciar entre Cliente o Empleado

        // Depuración
        System.out.println("Rol ID asignado al usuario: " + usuario.getIdRol());
        System.out.println("Se guardó usuario temporal: " + usuario);

        // Mostrar el contenido de pendingClients
        System.out.println("Contenido de pendingClients:");
        pendingClients.forEach((correo, user) -> System.out.println("Usuario: " + correo + " -> " + user));

        // Respuesta
        return ResponseEntity.ok(Map.of("message", "Código de verificación enviado al correo."));
    }
    @PostMapping("/validarCodigo")
    public ResponseEntity<Object> validarCodigo(@RequestBody AuthValidateCodRequest request) {
        String email = request.username();
        String code = request.code();

        // Validación básica
        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El correo y el código no pueden estar vacíos."));
        }

        // Verificación del código
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            UsuarioDto usuarioDto = pendingClients.get(email);

            if (usuarioDto == null) {
                return ResponseEntity.badRequest().body(Map.of("message", "Usuario no encontrado."));
            }
            //Si se llega usar codigo al correo para admin o terapeutas
            if(usuarioDto.getIdRol() >3){
                return ResponseEntity.badRequest().body(Map.of("message", "Codigo de Rol no existe"));
            }else{

                userDetailService.createUser(usuarioDto);
            }

            // Limpieza
            verificationCodes.remove(email);
            pendingClients.remove(email);

            return ResponseEntity.ok(Map.of("message", "Código validado. Tutor registrado exitosamente."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Código de verificación incorrecto."));
        }
    }
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
    try {
            AuthResponse response = this.userDetailService.loginUser(userRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(principal.getName());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        // Volver a consultar según el rol
        if (usuario.getRol().getName().equals("ROLE_TUTOR")) {

            TutorDto tutorDto = new TutorDto();
            tutorDto.setIdUsuario(usuario.getIdUsuario());
            tutorDto.setUsername(usuario.getUsername());
            tutorDto.setName(usuario.getName());
            tutorDto.setLastname(usuario.getLastname());
            tutorDto.setDni(usuario.getDni());
            tutorDto.setCreationDate(usuario.getCreationDate());
            tutorDto.setIdRol(usuario.getRol().getId());
            tutorDto.setDireccion(((Tutor) usuario).getDireccion());
            tutorDto.setTelefono(((Tutor) usuario).getTelefono());
            tutorDto.setEstado(((Tutor) usuario).getEstadoTutor());

            return ResponseEntity.ok(tutorDto);


        }  else if (usuario.getRol().getName().equals("ROLE_TERAPEUTA") || usuario.getRol().getName().equals("ROLE_ADMIN")) {
            Optional<Empleado> optionalEmp = empleadoRepository.findByDni(usuario.getDni());

            if (optionalEmp.isPresent()) {
                Empleado emp = optionalEmp.get();
                EmpleadoDto empleadoDto = new EmpleadoDto();
                empleadoDto.setIdUsuario(usuario.getIdUsuario());
                empleadoDto.setUsername(usuario.getUsername());
                empleadoDto.setName(usuario.getName());
                empleadoDto.setLastname(usuario.getLastname());
                empleadoDto.setDni(usuario.getDni());
                empleadoDto.setCreationDate(usuario.getCreationDate());
                empleadoDto.setIdRol(usuario.getRol().getId());
                empleadoDto.setEspecialidad(emp.getEspecialidad());
                empleadoDto.setEstadoEmpleado(emp.getEstadoEmpleado());
                return ResponseEntity.ok(empleadoDto);
            } else {
                // Aquí puedes decidir qué hacer si no encuentra el empleado
                // Por ejemplo, devolver un 404 o un mensaje de error:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empleado no encontrado con DNI: " + usuario.getDni());
            }
        }

        return ResponseEntity.badRequest().body("El usuario no es ni un Tutor ni un Empleado");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String username) {
        Optional<Usuario> optionalUsuario = usuarioService.obtenerUsuario(username);
        if (optionalUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = optionalUsuario.get();

        // Verificar si ya existe un token de restablecimiento para el usuario
        Optional<PasswordResetToken> existingToken = tokenRepository.findByUsuario(usuario);
        existingToken.ifPresent(tokenRepository::delete); // Eliminar token anterior si existe

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUsuario(usuario);
        resetToken.setExpiration(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        mailManager.enviarCorreoCambioPassword(username, token);
        return ResponseEntity.ok(Map.of("message", "Correo de recuperación enviado"));
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String nuevaContrasena) {
        Optional<PasswordResetToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido");
        }

        PasswordResetToken resetToken = optionalToken.get();
            // Depuración: Imprimir la hora actual y la expiración del token
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Hora actual: " + currentTime);
        System.out.println("Hora de expiración del token: " + resetToken.getExpiration());
        if (resetToken.getExpiration().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setPassword(new BCryptPasswordEncoder().encode(nuevaContrasena));
        usuarioService.Guardar(usuario);

        tokenRepository.delete(resetToken); // invalidar token
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }
    @PutMapping ("/completarPerfil")
    public ResponseEntity<Object> completarPerfil(@RequestBody TutorDto tutorDto) {

        // Verificar si el usuario existe
        Optional<Usuario> usuarioExistenteOptional = usuarioService.obtenerUsuario(tutorDto.getUsername());
        if (usuarioExistenteOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El usuario no existe.");
            return ResponseEntity.badRequest().body(response);
        }

        Usuario usuarioExistente = usuarioExistenteOptional.get();

        // Verificar si es instancia de Tutor
        if (!(usuarioExistente instanceof Tutor)) {
            return ResponseEntity.badRequest().body("Este endpoint es solo para tutores.");
        }

        Tutor tutorExistente = (Tutor) usuarioExistente;

        // Validación de campos actualizar
        if (tutorDto.getTelefono() != null && !tutorDto.getTelefono().isBlank()) {
            tutorExistente.setTelefono(tutorDto.getTelefono());
        }

        if (tutorDto.getDireccion() != null && !tutorDto.getDireccion().isBlank()) {
            tutorExistente.setDireccion(tutorDto.getDireccion());

        }

        // Actualización de campos heredados de Usuario
        tutorExistente.setName(tutorDto.getName());
        tutorExistente.setLastname(tutorDto.getLastname());
        tutorExistente.setDni(tutorDto.getDni());
        tutorExistente.setUsername(tutorDto.getUsername());
        if (tutorDto.getPassword() != null && !tutorDto.getPassword().isBlank()) {
            tutorExistente.setPassword(passwordEncoder.encode(tutorDto.getPassword()));
        }

        // Actualización de campos propios de Tutor
        tutorExistente.setDireccion(tutorDto.getDireccion());
        tutorExistente.setTelefono(tutorDto.getTelefono());

        // Guardar cambios
        usuarioService.actualizarUsuario(tutorExistente);

        // Respuesta de éxito
        Map<String, String> response = new HashMap<>();
        response.put("message", "Perfil completado y actualizado exitosamente.");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/auth/hello-secured1")
    public String hola() {


        return "hola admin";
    }

    @GetMapping("/tutores")
    public ResponseEntity<Object> obtenerTutores() {
        List<Usuario> tutores = usuarioService.ListarTutores();
        if (tutores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No se encontraron tutores"));
        }
        return ResponseEntity.ok(tutores); // Devolver lista de tutores
    }

    @PostMapping("/registrar-tutor")
    public ResponseEntity<Object> registrarTutorComoAdmin(@RequestBody UsuarioDto usuarioDto) {
    // Validaciones básicas
    if (usuarioDto.getUsername() == null || usuarioDto.getUsername().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "El correo es obligatorio."));
    }

    if (usuarioService.obtenerUsuario(usuarioDto.getUsername()).isPresent()) {
        return ResponseEntity.badRequest().body(Map.of("message", "El correo ya está registrado."));
    }

    // Validar que tenga rol de tutor
    if (usuarioDto.getIdRol() != 1) {
        return ResponseEntity.badRequest().body(Map.of("message", "Este endpoint solo es para registrar tutores."));
    }

    try {
        userDetailService.createUser(usuarioDto); // reutiliza el método existente
        return ResponseEntity.ok(Map.of("message", "Tutor registrado correctamente."));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error al registrar tutor.", "error", e.getMessage()));
    }
}

}
