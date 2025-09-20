package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/**
 * Repositorio de acceso a datos para la entidad User
 */
public interface UserCrudRepository extends JpaRepository<User, String> {


    /**
     * Busca a un usuario por su email
     */
    Optional<User> findByEmail(String email);


    /**
     * Busca a un usuario por su código
     */
    Optional<User> findByCodigo(String codigo);


    /**
     * Verifica si existe un usuario mediante el DNI
     */
    boolean existsByDni(String dni);


    /**
     * Verifica si existe un usuario mediante el email
     */
    boolean existsByEmail(String email);

}
