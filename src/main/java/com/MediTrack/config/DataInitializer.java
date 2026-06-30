package com.MediTrack.config;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.*;
import com.MediTrack.persistance.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

@Configuration
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClinicalShiftsCrudRepository clinicalShiftsCrud;

    @Autowired
    private SpecialtyCrudRepository specialtyCrud;

    @Autowired
    private MedicProfileCrudRepository medicProfileCrud;

    @Autowired
    private MedicShiftCrudRepository medicShiftCrud;

    @Autowired
    private AppointmentCrudRepository appointmentCrud;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@meditrack.com";
        User admin = userRepository.findByEmail(adminEmail).orElse(null);

        if (admin == null) {
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

        seedClinicalShifts();
        seedSpecialties();
        seedMedicProfile();
    }

    private void seedClinicalShifts() {
        if (clinicalShiftsCrud.count() == 0) {
            ClinicalShifts manana = new ClinicalShifts();
            manana.setNombre("MANANA");
            manana.setHoraInicio(LocalTime.of(7, 0));
            manana.setHoraFin(LocalTime.of(12, 0));
            clinicalShiftsCrud.save(manana);

            ClinicalShifts tarde = new ClinicalShifts();
            tarde.setNombre("TARDE");
            tarde.setHoraInicio(LocalTime.of(14, 0));
            tarde.setHoraFin(LocalTime.of(22, 0));
            clinicalShiftsCrud.save(tarde);

            System.out.println("Turnos clínicos creados: MAÑANA (07:00-12:00), TARDE (14:00-22:00)");
        }
    }

    private void seedSpecialties() {
        if (specialtyCrud.count() == 0) {
            Specialty cardio = new Specialty();
            cardio.setNombre("Cardiología");
            specialtyCrud.save(cardio);

            Specialty pedia = new Specialty();
            pedia.setNombre("Pediatría");
            specialtyCrud.save(pedia);

            Specialty neuro = new Specialty();
            neuro.setNombre("Neurología");
            specialtyCrud.save(neuro);

            System.out.println("Especialidades creadas: Cardiología, Pediatría, Neurología");
        }
    }

    private void seedMedicProfile() {
        User admin = userRepository.findByEmail("admin@meditrack.com").orElse(null);
        if (admin == null) return;

        if (!medicProfileCrud.existsByCodigoUsuario(admin.getCodigo())) {
            if (specialtyCrud.count() == 0 || clinicalShiftsCrud.count() == 0) return;

            MedicProfile perfil = new MedicProfile();
            perfil.setCodigoUsuario(admin.getCodigo());
            perfil.setFechaCreacion(LocalDateTime.now());
            perfil.setEspecialidades(new HashSet<>(specialtyCrud.findAll()));
            medicProfileCrud.save(perfil);

            List<ClinicalShifts> turnos = clinicalShiftsCrud.findAll();
            if (!turnos.isEmpty()) {
                MedicShift shift = new MedicShift();
                shift.setPerfilMedico(perfil);
                shift.setTurno(turnos.get(0));
                shift.setDiaSemana((short) 1);
                medicShiftCrud.save(shift);
            }

            System.out.println("Perfil médico creado para admin con especialidades y turno MAÑANA los Lunes");
        }
    }
}