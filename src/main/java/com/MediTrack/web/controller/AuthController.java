package com.MediTrack.web.controller;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.domain.service.MedicProfileService;
import com.MediTrack.domain.service.UserService;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        System.out.println("🔹 Intentando login con email: " + email);

        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        String rol = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        String token = jwtUtil.generarToken(email, rol);

        // Buscar usuario
        User usuario = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        System.out.println("✅ Usuario autenticado: " + usuario.getNombre() + " (" + rol + ")");
        System.out.println("Código de usuario: " + usuario.getCodigo());

        boolean tienePerfilMedico = false;
        if ("ROLE_MEDICO".equals(rol)) {
            System.out.println("🔎 Verificando si tiene perfil médico...");
            tienePerfilMedico = userService.tienePerfilMedico(usuario.getCodigo());
            System.out.println("➡️ Resultado tienePerfilMedico = " + tienePerfilMedico);
        }

        Map<String, Object> response = Map.ofEntries(
                Map.entry("token", token),
                Map.entry("rol", rol),
                Map.entry("codigo", usuario.getCodigo()),
                Map.entry("nombre", usuario.getNombre()),
                Map.entry("apellido", usuario.getApellido()),
                Map.entry("dni", usuario.getDni()),
                Map.entry("sexo", usuario.getSexo()),
                Map.entry("email", usuario.getEmail()),
                Map.entry("telefono", usuario.getTelefono()),
                Map.entry("activo", usuario.isActivo()),
                Map.entry("tienePerfilMedico", tienePerfilMedico)
        );

        System.out.println("📦 Respuesta enviada al frontend: " + response);
        return ResponseEntity.ok(response);
    }


}
