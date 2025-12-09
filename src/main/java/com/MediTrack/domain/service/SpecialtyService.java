package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.repository.SpecialtyRepository;
import com.MediTrack.persistance.entity.Specialty;
import com.MediTrack.persistance.mapper.SpecialtyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository repository;

    @Autowired
    private SpecialtyMapper mapper;

    public SpecialtyDTO save(SpecialtyDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la especialidad no puede estar vacío");
        }

        String nombre = dto.getNombre().trim();
        if (!nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
            throw new IllegalArgumentException("El nombre de la especialidad solo puede contener letras y espacios");
        }

        if (repository.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("La especialidad ya existe");
        }

        dto.setNombre(nombre);
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

    public SpecialtyDTO update(Long id, SpecialtyDTO dto) {
        SpecialtyDTO existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Especialidad no encontrada"));

        if (dto.getNombre() != null) {
            String nombre = dto.getNombre().trim();
            if (nombre.isEmpty() || !nombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) {
                throw new IllegalArgumentException("El nombre de la especialidad solo puede contener letras y espacios");
            }
            if (repository.existsByNombreIgnoreCase(nombre) && !nombre.equalsIgnoreCase(existing.getNombre())) {
                throw new IllegalArgumentException("La especialidad ya existe");
            }
            existing.setNombre(nombre);
        }

        return repository.save(existing);
    }


}
