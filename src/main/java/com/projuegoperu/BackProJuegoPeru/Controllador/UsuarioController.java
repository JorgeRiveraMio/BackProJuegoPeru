package com.projuegoperu.BackProJuegoPeru.Controllador;

import com.projuegoperu.BackProJuegoPeru.Models.DTO.*;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Tutor;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Empleado;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.PasswordResetToken;
import com.projuegoperu.BackProJuegoPeru.Models.Entity.Usuario;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.EstadoCliente;
import com.projuegoperu.BackProJuegoPeru.Models.Enums.TipoUsuario;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/security")
public class UsuarioController {

    @Autowired
    private AuthenticateService authenticate;

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService mailManager;

    // Un mapa temporal para guardar clientes pendientes de verificación
    private final Map<String, String> verificationCodes = new HashMap<>(); // Email -> Código

    private final Map<String, UsuarioDto> pendingClients = new HashMap<>(); // Email -> Cliente

    @PostMapping("/enviarCodigo")
    public ResponseEntity<Object> enviarCodigo(@RequestBody UsuarioDto usuario) {

        // Validar que el correo no esté vacío
        if (usuario.getUsername() == null || usuario.getUsername().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo electrónico no puede estar vacío.");
            return ResponseEntity.badRequest().body(response);
        }

        // Validar formato de correo electrónico
        if (!usuario.getUsername().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo electrónico no tiene un formato válido.");
            return ResponseEntity.badRequest().body(response);
        }

        // Verificar si el correo ya está registrado
        Optional<Usuario> usuarioDaoOptional = usuarioService.obtenerUsuario(usuario.getUsername());
        if (usuarioDaoOptional.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo ya está registrado.");
            return ResponseEntity.badRequest().body(response);
        }

        // Enviar el código de verificación al correo del usuario
        String code = authenticate.sendMessageUser(usuario.getUsername());

        // Guardar el correo y el código temporalmente
        verificationCodes.put(usuario.getUsername(), code);

        // Depuración: Mostrar los detalles del usuario (incluido el rol)
        if (usuario.getRol() != null) {
            System.out.println("Rol asignado al usuario: " + usuario.getRol().getName());
        } else {
            System.out.println("El usuario no tiene rol asignado.");
        }

            // Verificar si el objeto es un ClienteDto o EmpleadoDto
        if (usuario instanceof ClienteDto) {
            ClienteDto clienteDto = (ClienteDto) usuario;
            clienteDto.setTipoUsuario(TipoUsuario.CLIENTE); // Aseguramos el tipo de usuario
            clienteDto.setEstado(EstadoCliente.ACTIVO);
            pendingClients.put(clienteDto.getUsername(), clienteDto);
            System.out.println("Se guardó un Cliente: " + clienteDto);
        } else if (usuario instanceof EmpleadoDto) {
            EmpleadoDto empleadoDto = (EmpleadoDto) usuario;
            empleadoDto.setTipoUsuario(TipoUsuario.EMPLEADO); // Aseguramos el tipo de usuario
            pendingClients.put(empleadoDto.getUsername(), empleadoDto);
            System.out.println("Se guardó un Empleado: " + empleadoDto);
        } else {
            System.out.println("Tipo de usuario desconocido: " + usuario.getClass().getName());
            return ResponseEntity.badRequest().body("Tipo de usuario desconocido.");
        }

        // Mostrar el contenido de pendingClients para depuración
        System.out.println("Contenido de pendingClients:");
        for (Map.Entry<String, UsuarioDto> entry : pendingClients.entrySet()) {
            System.out.println("Usuario: " + entry.getKey() + " -> " + entry.getValue());
        }

        // Responder con un mensaje de éxito
        Map<String, String> response = new HashMap<>();
        response.put("message", "Código de verificación enviado al correo.");
        return ResponseEntity.ok(response);
    }



    
    // Endpoint para validar el código de verificación (unificado para Cliente y Empleado)
    @PostMapping("/validarCodigo")
    public ResponseEntity<Object> validarCodigo(@RequestBody AuthValidateCodRequest request) {
        String email = request.username();
        String code = request.code();

        // Validación de que el correo y el código no estén vacíos
        if (email == null || email.isEmpty() || code == null || code.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El correo y el código no pueden estar vacíos.");
            return ResponseEntity.badRequest().body(response);
        }

        // Verificar si el código ingresado coincide con el enviado
        if (verificationCodes.containsKey(email) && verificationCodes.get(email).equals(code)) {
            // Obtener el usuario temporal (cliente o empleado)
            UsuarioDto usuarioDto = pendingClients.get(email);

            if (usuarioDto == null) {
                System.out.println("No se encontró el usuario temporal para: " + email);
                return ResponseEntity.badRequest().body("Usuario no encontrado.");
            }

            // Verificar el tipo de usuario (Cliente o Empleado) con base en el TipoUsuario
            if (usuarioDto.getTipoUsuario() == TipoUsuario.CLIENTE) {
                ClienteDto cliente = (ClienteDto) usuarioDto;
                cliente.setPassword(cliente.getPassword()); // Encriptar la contraseña
                this.userDetailService.createUser(cliente);
            } else if (usuarioDto.getTipoUsuario() == TipoUsuario.EMPLEADO) {
                EmpleadoDto empleado = (EmpleadoDto) usuarioDto;
                empleado.setPassword(empleado.getPassword()); // Encriptar la contraseña
                this.userDetailService.createUser(empleado);
            } else {
                return ResponseEntity.badRequest().body("Tipo de usuario desconocido.");
            }

            // Eliminar el código y el usuario temporal
            verificationCodes.remove(email);
            pendingClients.remove(email);

            // Respuesta de éxito
            Map<String, String> response = new HashMap<>();
            response.put("message", "Código validado. Usuario registrado exitosamente.");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Código de verificación incorrecto.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/completarPerfil")
    public ResponseEntity<Object> completarPerfil(@RequestBody ClienteDto clienteDto) {
    
        // Validación: Verificar que el usuario existe
        Optional<Usuario> usuarioExistenteOptional = usuarioService.obtenerUsuario(clienteDto.getUsername());
        if (usuarioExistenteOptional.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El usuario no existe.");
            return ResponseEntity.badRequest().body(response);
        }
    
        Usuario usuarioExistente = usuarioExistenteOptional.get();
    
        // Verificamos si el usuario es un ClienteDto
        if (!(usuarioExistente instanceof Tutor)) {
            return ResponseEntity.badRequest().body("Este endpoint es solo para clientes.");
        }
    
        Tutor tutorExistente = (Tutor) usuarioExistente;
    
        // Validar los campos obligatorios del cliente
        if (clienteDto.getTelefono() == null || clienteDto.getTelefono().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El teléfono es obligatorio para completar el perfil.");
            return ResponseEntity.badRequest().body(response);
        }
        if (clienteDto.getDireccion() == null || clienteDto.getDireccion().isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "La dirección es obligatoria para completar el perfil.");
            return ResponseEntity.badRequest().body(response);
        }
    
        tutorExistente.setDireccion(clienteDto.getDireccion());
        tutorExistente.setTelefono(clienteDto.getTelefono());
    
        usuarioService.actualizarUsuario(tutorExistente);
    
        // Respuesta de éxito
        Map<String, String> response = new HashMap<>();
        response.put("message", "Perfil completado y actualizado exitosamente.");
        return ResponseEntity.ok(response);
    }
    



    @GetMapping("/actual-usuario")
    public ResponseEntity<?> obtenerUsuarioActual(Principal principal) {
        Optional<Usuario> usuarioOptional = usuarioService.obtenerUsuario(principal.getName());
        if (usuarioOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
        Usuario usuario = usuarioOptional.get();
        
        // Si el usuario es de tipo Cliente
        if (usuario instanceof Tutor) {
            ClienteDto clienteDto = new ClienteDto();
            clienteDto.setUsername(usuario.getUsername());
            clienteDto.setName(usuario.getName());
            clienteDto.setLastname(usuario.getLastname());
            clienteDto.setDni(usuario.getDni());
            clienteDto.setCreationDate(usuario.getCreationDate());
            clienteDto.setRol(new RolDto(usuario.getRol().getName()));
            clienteDto.setDireccion(((Tutor) usuario).getDireccion());
            clienteDto.setTelefono(((Tutor) usuario).getTelefono());
            clienteDto.setEstado(((Tutor) usuario).getEstadoCliente());
            
            return ResponseEntity.ok(clienteDto);
        }
        
        // Si el usuario es de tipo Empleado
        if (usuario instanceof Empleado) {
            EmpleadoDto empleadoDto = new EmpleadoDto();
            empleadoDto.setUsername(usuario.getUsername());
            empleadoDto.setName(usuario.getName());
            empleadoDto.setLastname(usuario.getLastname());
            empleadoDto.setDni(usuario.getDni());
            empleadoDto.setCreationDate(usuario.getCreationDate());
            empleadoDto.setRol(new RolDto(usuario.getRol().getName()));
            
            Empleado empleado = (Empleado) usuario;
            empleadoDto.setEspecialidad(empleado.getEspecialidad());
            empleadoDto.setEstadoEmpleado(empleado.getEstadoEmpleado());
            
            return ResponseEntity.ok(empleadoDto);
        }

        // Si no es ni Cliente ni Empleado
        return ResponseEntity.badRequest().body("El usuario no es ni un Cliente ni un Empleado");
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


    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthLoginRequest userRequest){
    try {
            AuthResponse response = this.userDetailService.loginUser(userRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
//        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }
}
