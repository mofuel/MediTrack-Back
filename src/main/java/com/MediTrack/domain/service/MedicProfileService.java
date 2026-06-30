package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.MedicProfileDTO;
import com.MediTrack.domain.dto.SpecialtyDTO;
import com.MediTrack.domain.repository.MedicProfileRepository;
import com.MediTrack.domain.repository.UserRepository;
import com.MediTrack.persistance.crud.SpecialtyCrudRepository;
import com.MediTrack.persistance.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicProfileService {

    @Autowired
    private MedicProfileRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecialtyCrudRepository specialtyCrud;

    public MedicProfileDTO save(MedicProfileDTO dto) {
        // 1. Validar que el usuario exista
        User user = userRepository.findByCodigo(dto.getCodigoUsuario())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuario con código " + dto.getCodigoUsuario() + " no existe"));

        // 2. Validar que sea médico
        if (!"ROLE_MEDICO".equals(user.getRol())) {
            throw new IllegalArgumentException(
                    "El usuario " + dto.getCodigoUsuario() + " no tiene rol de médico");
        }

        // 3. Validar que no tenga ya un perfil
        if (repository.existsByCodigoUsuario(dto.getCodigoUsuario())) {
            throw new IllegalArgumentException(
                    "El usuario " + dto.getCodigoUsuario() + " ya tiene un perfil médico");
        }

        // 4. Validar que las especialidades existan
        if (dto.getEspecialidades() != null) {
            for (SpecialtyDTO esp : dto.getEspecialidades()) {
                if (!specialtyCrud.existsById(esp.getId())) {
                    throw new IllegalArgumentException(
                            "La especialidad con ID " + esp.getId() + " no existe");
                }
            }
        }

        return repository.save(dto);
    }

    public Optional<MedicProfileDTO> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<MedicProfileDTO> findByCodigoUsuario(String codigoUsuario) {
        return repository.findByCodigoUsuario(codigoUsuario);
    }

    public List<MedicProfileDTO> findAll() {
        return repository.findAll();
    }

    public List<MedicProfileDTO> findByEspecialidadId(Long especialidadId) {
        return repository.findByEspecialidadId(especialidadId);
    }


    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
