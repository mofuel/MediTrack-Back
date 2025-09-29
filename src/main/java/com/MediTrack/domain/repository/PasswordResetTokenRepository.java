package com.MediTrack.domain.repository;

import com.MediTrack.persistance.entity.PasswordResetToken;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken token);
    Optional<PasswordResetToken> findByTokenHash(String hash);
    Optional<PasswordResetToken> findActiveByUserCodigo(String userCodigo);
    void deleteByUserCodigo(String userCodigo);
    List<PasswordResetToken> findAllByUserCodigo(String userCodigo);

}
