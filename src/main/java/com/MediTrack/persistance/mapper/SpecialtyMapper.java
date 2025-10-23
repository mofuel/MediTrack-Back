package com.MediTrack.persistance.mapper;


import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.persistance.entity.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    SpecialtyDTO toSpecialtyDTO(Specialty specialty);

    Specialty toSpecialty(SpecialtyDTO dto);
}
