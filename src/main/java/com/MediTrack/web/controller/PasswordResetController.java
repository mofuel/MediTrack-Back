package com.MediTrack.web.controller;


import com.MediTrack.domain.service.EmailService;
import com.MediTrack.domain.service.PasswordResetTokenService;
import com.MediTrack.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/request")
    public ResponseEntity<?> requestToken(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        var user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String message;
        boolean tokenAlreadyExists = false;

        try {
            String rawToken = tokenService.createToken(user.getCodigo());
            emailService.enviarCorreo(
                    email,
                    "Recuperación de contraseña - MediTrack",
                    "Tu código de recuperación es: " + rawToken +
                            "\nEste código expira en 15 minutos."
            );
            message = "Token enviado al correo";
        } catch (IllegalStateException e) {
            message = e.getMessage();
            tokenAlreadyExists = true;
        }

        return ResponseEntity.ok(Map.of(
                "message", message,
                "tokenAlreadyExists", tokenAlreadyExists
        ));
    }




    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String rawToken = body.get("token");

        var user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        boolean valid = tokenService.validateToken(rawToken, user.getCodigo());
        return ResponseEntity.ok(Map.of("valid", valid));
    }


    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String rawToken = body.get("token");
        String newPassword = body.get("newPassword");

        var user = userService.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!tokenService.validateToken(rawToken, user.getCodigo())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Token inválido o expirado"));
        }

        // Marca como usado
        tokenService.markAsUsed(rawToken, user.getCodigo());

        // Actualiza contraseña
        userService.updatePassword(user.getCodigo(), newPassword);

        return ResponseEntity.ok(Map.of("message", "Contraseña actualizada"));
    }

}
