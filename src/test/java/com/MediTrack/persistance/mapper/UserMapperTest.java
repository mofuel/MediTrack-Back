package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.UserDTO;
import com.MediTrack.persistance.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testToUsuariosDTO() {
        User user = new User();
        user.setCodigo("U001");
        user.setNombre("Miguel");
        user.setApellido("Monzón");
        user.setDni("12345678");
        user.setSexo("Masculino");
        user.setEmail("miguel@example.com");
        user.setTelefono("987654321");
        user.setRol("ROLE_ADMIN");
        user.setPassword("secure123");
        user.setActivo(true);

        UserDTO dto = mapper.toUsuariosDTO(user);

        assertEquals(user.getNombre(), dto.getNombre());
        assertEquals(user.getApellido(), dto.getApellido());
        assertEquals(user.getDni(), dto.getDni());
        assertEquals(user.getSexo(), dto.getSexo());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getTelefono(), dto.getTelefono());
        assertEquals(user.getRol(), dto.getRol());
        assertEquals(user.getPassword(), dto.getPassword());
        assertEquals(user.isActivo(), dto.isActivo());
    }

    @Test
    void testToUsuarios() {
        UserDTO dto = new UserDTO();
        dto.setNombre("Ana");
        dto.setApellido("López");
        dto.setDni("87654321");
        dto.setSexo("Femenino");
        dto.setEmail("ana@example.com");
        dto.setTelefono("912345678");
        dto.setRol("ROLE_USER");
        dto.setPassword("pass123");
        dto.setActivo(false);

        User user = mapper.toUsuarios(dto);

        assertEquals(dto.getNombre(), user.getNombre());
        assertEquals(dto.getApellido(), user.getApellido());
        assertEquals(dto.getDni(), user.getDni());
        assertEquals(dto.getSexo(), user.getSexo());
        assertEquals(dto.getEmail(), user.getEmail());
        assertEquals(dto.getTelefono(), user.getTelefono());
        assertEquals(dto.getRol(), user.getRol());
        assertEquals(dto.getPassword(), user.getPassword());
        assertEquals(dto.isActivo(), user.isActivo());
    }
}
