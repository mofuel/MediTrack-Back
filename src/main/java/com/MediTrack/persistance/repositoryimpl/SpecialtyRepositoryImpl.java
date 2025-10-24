package com.MediTrack.persistance.repositoryimpl;

import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.repository.SpecialtyRepository;
import com.MediTrack.persistance.crud.SpecialtyCrudRepository;
import com.MediTrack.persistance.entity.Specialty;
import com.MediTrack.persistance.mapper.SpecialtyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class SpecialtyRepositoryImpl implements SpecialtyRepository {

    @Autowired
    private SpecialtyCrudRepository crud;

    @Autowired
    private SpecialtyMapper mapper;

    @Override
    public SpecialtyDTO save(SpecialtyDTO dto) {
        Specialty specialty = mapper.toSpecialty(dto);
        Specialty saved = crud.save(specialty);
        return mapper.toSpecialtyDTO(saved);
    }

    @Override
    public Optional<SpecialtyDTO> findById(Long id) {
        return crud.findById(id)
                .map(mapper::toSpecialtyDTO);
    }

    @Override
    public Optional<SpecialtyDTO> findByNombre(String nombre) {
        return crud.findByNombre(nombre)
                .map(mapper::toSpecialtyDTO);
    }

    @Override
    public List<SpecialtyDTO> findAll() {
        return crud.findAll()
                .stream()
                .map(mapper::toSpecialtyDTO)
                .toList();
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return crud.existsByNombre(nombre);
    }

    @Override
    public void delete(Long id) {
        crud.deleteById(id);
    }
}
