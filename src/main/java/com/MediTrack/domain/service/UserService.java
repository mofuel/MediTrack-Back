package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.RegisterDTO;
import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;
import com.MediTrack.persistance.entity.MedicProfile;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.persistance.mapper.RegisterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private MedicProfileCrudRepository crud;


    private void validarDatosRegistro(RegisterDTO dto) {
        if (dto.getDni() == null || !dto.getDni().matches("\\d{8}")) {
            throw new IllegalArgumentException("El DNI debe tener 8 dígitos");
        }

        if (dto.getTelefono() == null || !dto.getTelefono().matches("\\d{9}")) {
            throw new IllegalArgumentException("El número de celular debe tener 9 dígitos");
        }

        if (dto.getEmail() == null || !dto.getEmail().contains("@")) {
            throw new IllegalArgumentException("Correo electrónico inválido");
        }
    }




    public User guardar(User user) {
        if (user.getCodigo() == null || user.getCodigo().isEmpty()) {
            user.setCodigo(generarCodigoPersonalizado());
        }
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            System.err.println("Falló el guardado!");
        } else {
            System.out.println("Usuario guardado con código: " + savedUser.getCodigo());
        }
        return savedUser;
    }



    public User registrarUsuario(RegisterDTO dto) {
        validarDatosRegistro(dto); // <-- validaciones
        User user = registerMapper.toUserFromRegisterDTO(dto);
        user.setRol("ROLE_PACIENTE");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActivo(true);
        return guardar(user);
    }

    public User registrarDoctor(RegisterDTO dto) {
        validarDatosRegistro(dto); // <-- validaciones
        User user = registerMapper.toUserFromRegisterDTO(dto);
        user.setRol("ROLE_MEDICO");
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActivo(true);
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

    @Transactional
    public void deleteByCodigo(String codigo) {
        userRepository.deleteByCodigo(codigo);
    }

    public User actualizarUsuario(String codigo, User datosActualizados) {
        Optional<User> existenteOpt = userRepository.findByCodigo(codigo);
        if (existenteOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con código: " + codigo);
        }

        User existente = existenteOpt.get();

        if (datosActualizados.getNombre() != null) existente.setNombre(datosActualizados.getNombre());
        if (datosActualizados.getApellido() != null) existente.setApellido(datosActualizados.getApellido());
        if (datosActualizados.getDni() != null) existente.setDni(datosActualizados.getDni());
        if (datosActualizados.getSexo() != null) existente.setSexo(datosActualizados.getSexo());
        if (datosActualizados.getEmail() != null) existente.setEmail(datosActualizados.getEmail());
        if (datosActualizados.getTelefono() != null) existente.setTelefono(datosActualizados.getTelefono());
        if (datosActualizados.getRol() != null) existente.setRol(datosActualizados.getRol());

        if (datosActualizados.isActivo() != existente.isActivo()) {
            existente.setActivo(datosActualizados.isActivo());
        }

        return userRepository.save(existente);
    }

    public boolean tienePerfilMedico(String codigoUsuario) {

        return crud.findByCodigoUsuario(codigoUsuario)
                .map(perfil -> {
                    if (perfil.getEspecialidades() == null || perfil.getEspecialidades().isEmpty()) {
                        return false;
                    }
                    return true;
                })
                .orElseGet(() -> {
                    return false;
                });
    }

    public List<User> findByRol(String rol) {
        return userRepository.findByRol(rol);
    }


}
