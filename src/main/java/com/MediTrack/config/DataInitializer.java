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
        String adminEmail = "admin@meditrack.com";
        User admin = userRepository.findByEmail(adminEmail).orElse(null);

        if (admin == null) {
            // Si no existe, creamos el admin
            User nuevoAdmin = new User();
            nuevoAdmin.setCodigo("U001");
            nuevoAdmin.setNombre("Administrador");
            nuevoAdmin.setApellido("Del Sistema");
            nuevoAdmin.setDni("11111111");
            nuevoAdmin.setSexo("Masculino");
            nuevoAdmin.setTelefono("111111111");
            nuevoAdmin.setEmail(adminEmail);
            nuevoAdmin.setPassword(passwordEncoder.encode("admin123"));
            nuevoAdmin.setRol("ROLE_ADMIN");
            nuevoAdmin.setActivo(true);

            userRepository.save(nuevoAdmin);
            System.out.println("Usuario admin creado correctamente: admin@meditrack.com / admin123");
        } else {
            admin.setNombre("Administrador");
            admin.setApellido("Del Sistema");
            admin.setDni("11111111");
            admin.setSexo("Masculino");
            admin.setTelefono("111111111");
            admin.setRol("ROLE_ADMIN");
            admin.setActivo(true);
            userRepository.save(admin);
            System.out.println("Usuario administrador ya existía, datos actualizados si era necesario.");
        }
    }
}
