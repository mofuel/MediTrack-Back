package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.repository.PasswordResetTokenRepository;
import com.MediTrack.persistance.crud.PasswordResetTokenCrudRepository;
import com.MediTrack.persistance.entity.PasswordResetToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    @Autowired
    private PasswordResetTokenCrudRepository crud;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return crud.save(token);
    }

    @Override
    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return crud.findByTokenHash(tokenHash);
    }

    @Override
    public Optional<PasswordResetToken> findActiveByUserCodigo(String userCodigo) {
        return crud.findByUserCodigoAndUsedFalseAndExpiresAtAfter(userCodigo, LocalDateTime.now());
    }

    @Override
    public void deleteByUserCodigo(String userCodigo) {
        crud.deleteByUserCodigo(userCodigo);
    }

    @Override
    public List<PasswordResetToken> findAllByUserCodigo(String userCodigo) {
        return crud.findAllByUserCodigo(userCodigo);
    }
}
