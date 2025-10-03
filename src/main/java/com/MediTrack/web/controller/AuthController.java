package com.MediTrack.web.controller;

import com.MediTrack.domain.repository.UserRepository;
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
@RequestMapping("/auth")
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

        // Autentica al usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Obtén el rol
        String rol = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");

        // Genera token
        String token = jwtUtil.generarToken(email, rol);

        // Traer usuario completo desde DB
        User usuario = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Devuelve token + info completa del usuario
        Map<String, Object> response = Map.of(
                "token", token,
                "rol", rol,
                "codigo", usuario.getCodigo(),
                "nombre", usuario.getNombre(),
                "apellido", usuario.getApellido(),
                "dni", usuario.getDni(),
                "sexo", usuario.getSexo(),
                "email", usuario.getEmail(),
                "telefono", usuario.getTelefono(),
                "activo", usuario.isActivo()
        );

        return ResponseEntity.ok(response);
    }
}
