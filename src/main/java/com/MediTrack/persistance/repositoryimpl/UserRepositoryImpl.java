package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.UserCrudRepository;
import com.MediTrack.persistance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserCrudRepository crud;

    @Override
    public User save(User user) {return crud.save(user);}

    @Override
    public Optional<User> findByEmail(String email) {
        return crud.findByEmail(email);
    }

    @Override
    public Optional<User> findByCodigo(String codigo) {
        return crud.findByCodigo(codigo);
    }

    @Override
    public boolean existsByEmail(String email) {
        return crud.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return crud.existsByDni(dni);
    }

    @Override
    public void updatePassword(String codigo, String encodedPassword) {
        crud.findByCodigo(codigo).ifPresent(user -> {
            user.setPassword(encodedPassword);
            crud.save(user);
        });
    }

    @Override
    public long count() { return crud.count(); }

    @Override
    public void deleteByCodigo(String codigo) {
        crud.deleteByCodigo(codigo);
    }

    @Override
    public List<User> findAll() {
        return crud.findAll();
    }

    @Override
    public List<User> findByRol(String rol) {
        return crud.findByRol(rol);
    }


}
