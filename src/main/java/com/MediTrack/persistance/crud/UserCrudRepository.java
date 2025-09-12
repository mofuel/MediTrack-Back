package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCrudRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByCodigo(String codigo);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);

}
