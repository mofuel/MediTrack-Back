package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.domain.repository.MedicProfileRepository;
import com.MediTrack.persistance.crud.MedicProfileCrudRepository;
import com.MediTrack.persistance.entity.MedicProfile;
import com.MediTrack.persistance.mapper.MedicProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class MedicProfileRepositoryImpl implements MedicProfileRepository {

    @Autowired
    private MedicProfileCrudRepository crud;

    @Autowired
    private MedicProfileMapper mapper;

    @Override
    public MedicProfileDTO save(MedicProfileDTO dto) {
        MedicProfile profile = mapper.toMedicProfile(dto);
        MedicProfile saved = crud.save(profile);
        return mapper.toMedicProfileDTO(saved);
    }

    @Override
    public Optional<MedicProfileDTO> findById(Long id) {
        return crud.findById(id).map(mapper::toMedicProfileDTO);
    }

    @Override
    public Optional<MedicProfileDTO> findByCodigoUsuario(String codigoUsuario) {
        return crud.findByCodigoUsuario(codigoUsuario)
                .map(mapper::toMedicProfileDTO);
    }

    @Override
    public List<MedicProfileDTO> findAll() {
        return crud.findAll()
                .stream()
                .map(mapper::toMedicProfileDTO)
                .toList();
    }

    @Override
    public boolean existsByCodigoUsuario(String codigoUsuario) {
        return crud.existsByCodigoUsuario(codigoUsuario);
    }

    @Override
    public void deleteById(Long id) {
        crud.deleteById(id);
    }
}