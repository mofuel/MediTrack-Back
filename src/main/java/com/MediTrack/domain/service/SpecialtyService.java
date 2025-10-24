package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository repository;

    public SpecialtyDTO save(SpecialtyDTO dto) {
        if (repository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("La especialidad ya existe");
        }
        return repository.save(dto);
    }

    public Optional<SpecialtyDTO> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<SpecialtyDTO> getByNombre(String nombre) {
        return repository.findByNombre(nombre);
    }

    public List<SpecialtyDTO> getAll() {
        return repository.findAll();
    }

    public boolean delete(Long id) {
        Optional<SpecialtyDTO> specialty = repository.findById(id);
        if (specialty.isPresent()) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
