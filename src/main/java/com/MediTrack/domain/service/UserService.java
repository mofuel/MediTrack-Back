package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.RegisterDTO;
import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.persistance.mapper.RegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RegisterMapper registerMapper;



    public User guardar(User user) {
        // ✅ Si no viene código, lo generamos
        if (user.getCodigo() == null || user.getCodigo().isEmpty()) {
            user.setCodigo(generarCodigoPersonalizado());
        }
        User savedUser = userRepository.save(user);

        // ❌ Ya NO existe savedUser.getId(), así que eliminamos esa parte
        if (savedUser == null) {
            System.err.println("❌ Falló el guardado!");
        } else {
            System.out.println("✅ Usuario guardado con código: " + savedUser.getCodigo());
        }
        return savedUser;
    }



    public User registrarUsuario(RegisterDTO dto) {
        User user = registerMapper.toUserFromRegisterDTO(dto);
        user.setRol("ROLE_PACIENTE");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        System.out.println("Nuevo usuario: " + user);

        // Guardar y retornar el usuario persistido
        return guardar(user);
    }





    // Búsqueda por email
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Generar código
    private String generarCodigoPersonalizado() {
        long count = userRepository.count() + 1;
        return String.format("U%05d", count); // ejemplo: U00001
    }


    public void updatePassword(String codigo, String rawPassword) {
        String encoded = passwordEncoder.encode(rawPassword);
        userRepository.updatePassword(codigo, encoded);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByCodigo(String codigo) {
        return userRepository.findByCodigo(codigo);
    }

    public void deleteByCodigo(String codigo) {
        userRepository.deleteByCodigo(codigo);
    }


}
