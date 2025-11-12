package com.MediTrack.domain.repository;

import com.MediTrack.persistance.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByCodigo(String codigo);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    void updatePassword(String codigo, String encodedPassword);

    List<User> findAll();

    List<User> findByRol(String rol);

    void deleteByCodigo(String codigo);

    long count();
}
