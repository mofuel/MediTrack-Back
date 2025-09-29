package com.MediTrack.domain.service;


import com.MediTrack.domain.dto.PasswordResetTokenDTO;
import com.MediTrack.domain.repository.PasswordResetTokenRepository;
import com.MediTrack.persistance.entity.PasswordResetToken;
import com.MediTrack.persistance.mapper.PasswordResetTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenMapper tokenMapper;


    /** Genera un token, lo hashea y lo guarda */
    public String createToken(String userCodigo) {
        Optional<PasswordResetToken> activo = tokenRepository.findActiveByUserCodigo(userCodigo);
        if (activo.isPresent()) {
            throw new IllegalStateException("Ya existe un token activo. Revisa tu correo.");
        }

        String rawToken = UUID.randomUUID().toString();
        String hashedToken = BCrypt.hashpw(rawToken, BCrypt.gensalt());

        PasswordResetToken token = new PasswordResetToken();
        token.setUserCodigo(userCodigo);
        token.setTokenHash(hashedToken); // guardamos el hash
        token.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        token.setUsed(false);

        tokenRepository.save(token);

        // Devolvemos solo el token plano para enviar por correo
        return rawToken;
    }


    /**
     * Opción alternativa: invalida los tokens anteriores y genera uno nuevo
     * (si prefieres permitir siempre una nueva solicitud).
     */
    public PasswordResetToken createTokenInvalidateOld(String userCodigo, int minutesValid) {
        tokenRepository.deleteByUserCodigo(userCodigo);

        String rawToken = UUID.randomUUID().toString();
        String hashedToken = BCrypt.hashpw(rawToken, BCrypt.gensalt());

        PasswordResetToken token = new PasswordResetToken();
        token.setUserCodigo(userCodigo);
        token.setTokenHash(hashedToken); // ✅ ahora sí es un hash válido
        token.setExpiresAt(LocalDateTime.now().plusMinutes(minutesValid));
        token.setUsed(false);

        tokenRepository.save(token);
        return token;
    }




    /** Valida que el token exista, no esté usado ni vencido */
    public boolean validateToken(String rawToken, String userCodigo) {
        // Obtenemos todos los tokens del usuario
        List<PasswordResetToken> tokens = tokenRepository.findAllByUserCodigo(userCodigo);

        // Buscamos el token que haga match con el hash
        Optional<PasswordResetToken> match = tokens.stream()
                .filter(t -> {
                    try {
                        return BCrypt.checkpw(rawToken, t.getTokenHash());
                    } catch (IllegalArgumentException e) {
                        // Si el hash no es válido, lo ignoramos
                        return false;
                    }
                })
                .findFirst();

        if (match.isEmpty()) return false;

        PasswordResetToken token = match.get();
        // Retornamos true solo si no fue usado y no expiró
        return !token.isUsed() && token.getExpiresAt().isAfter(LocalDateTime.now());
    }





    /** Marca el token como usado */
    public void markAsUsed(String rawToken, String userCodigo) {
        List<PasswordResetToken> tokens = tokenRepository.findAllByUserCodigo(userCodigo);

        tokens.stream()
                .filter(t -> BCrypt.checkpw(rawToken, t.getTokenHash()))
                .findFirst()
                .ifPresent(t -> {
                    t.setUsed(true);
                    tokenRepository.save(t);
                });
    }


    public Optional<PasswordResetTokenDTO> findDtoByTokenHash(String tokenHash) {
        return tokenRepository.findByTokenHash(tokenHash)
                .map(tokenMapper::toDto);
    }
}


