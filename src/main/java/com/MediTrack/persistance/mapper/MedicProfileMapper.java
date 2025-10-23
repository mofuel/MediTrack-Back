package com.MediTrack.persistance.mapper;


import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.persistance.entity.MedicProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SpecialtyMapper.class})
public interface MedicProfileMapper {

    @Mapping(source = "user.nombre", target = "nombreCompleto")
    MedicProfileDTO toMedicProfileDTO(MedicProfile entity);

    MedicProfile toMedicProfile(MedicProfileDTO dto);
}
