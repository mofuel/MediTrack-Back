package com.MediTrack.config;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@meditrack.com").isEmpty()) {
            User admin = new User();
            admin.setCodigo("U001");
            admin.setNombre("Administrador");
            admin.setApellido("Del Sistema");
            admin.setDni("11111111");
            admin.setSexo("Masculino");
            admin.setTelefono("111111111");
            admin.setEmail("admin@meditrack.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRol("ROLE_ADMIN");
            admin.setActivo(true);

            userRepository.save(admin);
            System.out.println("✅ Usuario admin creado correctamente: admin@meditrack.com / admin123");
        } else {
            System.out.println("ℹ️ El usuario administrador ya existe.");
        }
    }
}
