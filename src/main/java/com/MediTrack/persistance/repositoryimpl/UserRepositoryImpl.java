package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.UserCrudRepository;
import com.MediTrack.persistance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserCrudRepository crud;

    @Override
    public User save(User user) {
        return crud.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByCodigo(String codigo) {
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByDni(String dni) {
        return false;
    }

    @Override
    public long count() {
        return 0;
    }
}
