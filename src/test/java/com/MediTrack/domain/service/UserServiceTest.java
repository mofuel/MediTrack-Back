package com.MediTrack.domain.service;

import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;
import com.MediTrack.persistance.entity.MedicProfile;
import com.MediTrack.persistance.entity.Specialty;
import com.MediTrack.persistance.mapper.RegisterMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private MedicProfileCrudRepository crud;

    @InjectMocks
    private UserService userService;

    @Test
    void testTienePerfilMedico_conEspecialidades() {
        // Arrange
        String codigoUsuario = "U00001";

        Specialty especialidad = new Specialty();
        especialidad.setNombre("Cardiología");

        MedicProfile perfil = new MedicProfile();
        perfil.setEspecialidades(Set.of(especialidad)); // ✅ tipo correcto

        when(crud.findByCodigoUsuario(codigoUsuario)).thenReturn(Optional.of(perfil));

        // Act
        boolean resultado = userService.tienePerfilMedico(codigoUsuario);

        // Assert
        assertTrue(resultado);
    }

    @Test
    void testTienePerfilMedico_sinPerfil() {
        String codigoUsuario = "U99999";
        when(crud.findByCodigoUsuario(codigoUsuario)).thenReturn(Optional.empty());

        boolean resultado = userService.tienePerfilMedico(codigoUsuario);

        assertFalse(resultado);
    }

    @Test
    void testTienePerfilMedico_sinEspecialidades() {
        String codigoUsuario = "U00002";

        MedicProfile perfil = new MedicProfile();
        perfil.setEspecialidades(Set.of()); // ✅ Set vacío

        when(crud.findByCodigoUsuario(codigoUsuario)).thenReturn(Optional.of(perfil));

        boolean resultado = userService.tienePerfilMedico(codigoUsuario);

        assertFalse(resultado);
    }
}