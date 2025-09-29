package com.MediTrack.persistance.mapper;

import com.MediTrack.domain.dto.PasswordResetTokenDTO;
import com.MediTrack.persistance.entity.PasswordResetToken;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordResetTokenMapper {

    PasswordResetTokenDTO toDto(PasswordResetToken entity);

    PasswordResetToken toEntity(PasswordResetTokenDTO dto);
}
