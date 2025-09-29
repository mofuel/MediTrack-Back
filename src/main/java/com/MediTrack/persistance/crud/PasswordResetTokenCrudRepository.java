package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenCrudRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    // Busca un token activo (no usado)
    Optional<PasswordResetToken>
    findByUserCodigoAndUsedFalseAndExpiresAtAfter(String userCodigo, LocalDateTime now);

    // Limpiar tokens antiguos
    void deleteByUserCodigo(String userCodigo);

    List<PasswordResetToken> findAllByUserCodigo(String userCodigo);


}
