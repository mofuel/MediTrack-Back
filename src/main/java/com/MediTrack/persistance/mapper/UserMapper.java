package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.UserDTO;
import com.MediTrack.persistance.entity.User;
import org.mapstruct.Mapper;

/**
 * Mapper de MapStruct para convertir entre la entidad User
 * y el DTO UserDTO
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Convierte User a UserDTO
     */
    UserDTO toUsuariosDTO(User user);


    /**
     * Convierte UserDTO a User
     */
    User toUsuarios(UserDTO userDTO);
}


