package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.RegisterDTO;
import com.MediTrack.domain.service.UserService;
import com.MediTrack.persistance.entity.User;
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

    /**
     * Registro de nuevos usuarios (pacientes por defecto)
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "❌ Las contraseñas no coinciden")
            );
        }

        Optional<User> existente = userService.buscarPorEmail(registerDTO.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "❌ El email ya está registrado")
            );
        }

        // 🔸 Registrar el usuario en BD
        User nuevoUsuario = userService.registrarUsuario(registerDTO);

        // 🔸 Crear respuesta JSON esperada por el frontend
        Map<String, Object> response = Map.of(
                "codigo", nuevoUsuario.getCodigo(),
                "nombre", nuevoUsuario.getNombre(),
                "apellido", nuevoUsuario.getApellido(),
                "dni", nuevoUsuario.getDni(),
                "sexo", nuevoUsuario.getSexo(),
                "email", nuevoUsuario.getEmail(),
                "telefono", nuevoUsuario.getTelefono(),
                "rol", nuevoUsuario.getRol() != null ? nuevoUsuario.getRol() : "ROLE_PACIENTE",

                "estado", nuevoUsuario.isActivo() ? "Activo" : "Inactivo",
                "token", "" // 🔹 Opcional, si aún no generas JWT aquí
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
                    .body(Map.of("error", "❌ Usuario no encontrado"));
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
                    .body(Map.of("error", "❌ No se encontró el usuario con ese código"));
        }
        userService.deleteByCodigo(codigo);
        return ResponseEntity.ok(Map.of("message", "✅ Usuario eliminado correctamente"));
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
                    .body(Map.of("error", "❌ Usuario no encontrado"));
        }

        userService.updatePassword(codigo, nuevaPassword);
        return ResponseEntity.ok(Map.of("message", "🔑 Contraseña actualizada correctamente"));
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
