package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.RegisterDTO;
import com.MediTrack.domain.service.UserService;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Registro de nuevos usuarios (pacientes por defecto)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Las contraseñas no coinciden"));
        }

        Optional<User> existente = userService.buscarPorEmail(registerDTO.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "El email ya está registrado"));
        }

        try {
            User nuevoUsuario = userService.registrarUsuario(registerDTO);
            String token = jwtUtil.generarToken(nuevoUsuario.getEmail(), nuevoUsuario.getRol());
            Map<String, Object> response = Map.of(
                    "codigo", nuevoUsuario.getCodigo(),
                    "nombre", nuevoUsuario.getNombre(),
                    "apellido", nuevoUsuario.getApellido(),
                    "dni", nuevoUsuario.getDni(),
                    "sexo", nuevoUsuario.getSexo(),
                    "email", nuevoUsuario.getEmail(),
                    "telefono", nuevoUsuario.getTelefono(),
                    "rol", nuevoUsuario.getRol(),
                    "estado", nuevoUsuario.isActivo() ? "Activo" : "Inactivo",
                    "token", token
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }




    @PostMapping("/doctors")
    public ResponseEntity<?> registrarDoctor(@RequestBody RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Las contraseñas no coinciden")
            );
        }

        Optional<User> existente = userService.buscarPorEmail(dto.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "El email ya está registrado")
            );
        }

        User nuevoDoctor = userService.registrarDoctor(dto);

        Map<String, Object> response = Map.of(
                "codigo", nuevoDoctor.getCodigo(),
                "nombre", nuevoDoctor.getNombre(),
                "apellido", nuevoDoctor.getApellido(),
                "dni", nuevoDoctor.getDni(),
                "sexo", nuevoDoctor.getSexo(),
                "email", nuevoDoctor.getEmail(),
                "telefono", nuevoDoctor.getTelefono(),
                "rol", nuevoDoctor.getRol(),
                "estado", nuevoDoctor.isActivo() ? "Activo" : "Inactivo"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    /**
     * Listar todos los usuarios (solo ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> listarUsuarios() {
        List<User> usuarios = userService.findAll();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Buscar usuario por código
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @GetMapping("/{codigo}")
    public ResponseEntity<?> obtenerUsuario(@PathVariable String codigo) {
        Optional<User> usuario = userService.findByCodigo(codigo);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }
    }

    /**
     * Eliminar usuario por código (solo ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable String codigo) {
        Optional<User> usuario = userService.findByCodigo(codigo);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No se encontró el usuario con ese código"));
        }
        userService.deleteByCodigo(codigo);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }

    /**
     * Obtener solo pacientes (para el admin)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pacientes")
    public ResponseEntity<List<User>> listarPacientes() {
        List<User> pacientes = userService.findByRol("ROLE_PACIENTE");
        return ResponseEntity.ok(pacientes);
    }

    /**
     * Actualizar contraseña
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
    @PutMapping("/{codigo}/password")
    public ResponseEntity<Map<String, String>> actualizarPassword(@PathVariable String codigo,
                                                                  @RequestBody String nuevaPassword) {
        Optional<User> usuario = userService.findByCodigo(codigo);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario no encontrado"));
        }

        userService.updatePassword(codigo, nuevaPassword);
        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada correctamente"));
    }

    /**
     * Actualizar usuario
     */
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable String codigo,
            @RequestBody User datosActualizados) {
        try {
            User actualizado = userService.actualizarUsuario(codigo, datosActualizados);
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
