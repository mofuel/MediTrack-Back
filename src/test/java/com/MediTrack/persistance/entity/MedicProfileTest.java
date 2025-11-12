package com.MediTrack.persistance.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import com.MediTrack.persistance.crud.UserCrudRepository;
import com.MediTrack.persistance.crud.SpecialtyCrudRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL"
})
class MedicProfileTest {

    @Autowired
    private MedicProfileCrudRepository medicProfileRepo;

    @Autowired
    private UserCrudRepository userRepo;

    @Autowired
    private SpecialtyCrudRepository specialtyRepo;

    @Test
    @DisplayName("✅ Debería guardar y recuperar un perfil médico con su usuario y especialidad asociada")
    void testGuardarYRecuperarPerfilMedico() {
        // 🧱 Crear y guardar usuario base
        User user = new User();
        user.setCodigo("U001");
        user.setNombre("Carlos");
        user.setApellido("Ramirez");
        user.setEmail("carlos@example.com");
        user.setPassword("123456");
        user.setRol("ROLE_MEDICO");
        user.setActivo(true);
        userRepo.save(user);

        // 🧱 Crear y guardar especialidad
        Specialty esp = new Specialty();
        esp.setNombre("Cardiología");
        specialtyRepo.save(esp);

        // 🧱 Crear y guardar perfil médico
        MedicProfile perfil = new MedicProfile();
        perfil.setCodigoUsuario(user.getCodigo());
        perfil.setFechaCreacion(LocalDateTime.now());
        perfil.setUser(user); // 🔗 Relación directa
        perfil.setEspecialidades(Set.of(esp));

        MedicProfile guardado = medicProfileRepo.save(perfil);

        // 🔍 Recuperar desde la base de datos
        MedicProfile encontrado = medicProfileRepo.findById(guardado.getId()).orElse(null);

        // ✅ Verificaciones
        assertThat(encontrado).isNotNull();
        assertThat(encontrado.getId()).isNotNull();
        assertThat(encontrado.getCodigoUsuario()).isEqualTo("U001");
        assertThat(encontrado.getUser()).isNotNull();
        assertThat(encontrado.getUser().getEmail()).isEqualTo("carlos@example.com");
        assertThat(encontrado.getEspecialidades()).hasSize(1);
        assertThat(encontrado.getEspecialidades().iterator().next().getNombre()).isEqualTo("Cardiología");
    }
}
