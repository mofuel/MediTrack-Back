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
    public ResponseEntity<String> registerUser(@RequestBody RegisterDTO registerDTO) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("❌ Las contraseñas no coinciden");
        }

        Optional<User> existente = userService.buscarPorEmail(registerDTO.getEmail());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ El email ya está registrado");
        }

        userService.registrarUsuario(registerDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("✅ Usuario registrado correctamente");
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
                    .body("❌ Usuario no encontrado");
        }
    }


    /**
     * Eliminar usuario por código (solo ADMIN)
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{codigo}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String codigo) {
        Optional<User> usuario = userService.findByCodigo(codigo);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ No se encontró el usuario con ese código");
        }
        userService.deleteByCodigo(codigo);
        return ResponseEntity.ok("✅ Usuario eliminado correctamente");
    }


    /**
     * Actualizar contraseña
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'PACIENTE', 'MEDICO')")
    @PutMapping("/{codigo}/password")
    public ResponseEntity<String> actualizarPassword(@PathVariable String codigo,
                                                     @RequestBody String nuevaPassword) {
        Optional<User> usuario = userService.findByCodigo(codigo);
        if (usuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ Usuario no encontrado");
        }

        userService.updatePassword(codigo, nuevaPassword);
        return ResponseEntity.ok("🔑 Contraseña actualizada correctamente");
    }
}
