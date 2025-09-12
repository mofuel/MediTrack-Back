package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.RegisterDTO;
import com.MediTrack.persistance.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(target = "id", ignore = true)       // Se genera automáticamente en la DB
    @Mapping(target = "codigo", ignore = true)   // Se genera luego del guardado
    @Mapping(target = "rol", ignore = true)      // Se asigna en el servicio (ej. "estudiante")
    @Mapping(target = "activo", ignore = true)   // Se activa por defecto
    User toUserFromRegisterDTO(RegisterDTO dto);
}
