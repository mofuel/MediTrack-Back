package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.MedicShiftDTO;
import com.MediTrack.domain.service.MedicShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medic-shifts")
public class MedicShiftController {

    @Autowired
    private MedicShiftService service;

    /**
     * Crear o asignar un turno a un médico
     * Solo ADMIN puede hacerlo
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/assign")
    public ResponseEntity<?> assignShift(@RequestBody MedicShiftDTO dto) {
        // Evita duplicar turno por día
        if (service.existsByPerfilAndDia(dto.getPerfilId(), dto.getDiaSemana())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El médico ya tiene asignado un turno para ese día");
        }

        MedicShiftDTO saved = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Listar todos los turnos asignados
     */
    // @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @GetMapping
    public ResponseEntity<List<MedicShiftDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Obtener turnos por ID de perfil médico
     */
    // @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @GetMapping("/perfil/{perfilId}")
    public ResponseEntity<List<MedicShiftDTO>> getByPerfil(@PathVariable Long perfilId) {
        return ResponseEntity.ok(service.findByPerfilId(perfilId));
    }

    /**
     * Obtener turnos por ID de turno clínico (mañana/noche)
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/turno/{turnoId}")
    public ResponseEntity<List<MedicShiftDTO>> getByTurno(@PathVariable Long turnoId) {
        return ResponseEntity.ok(service.findByTurnoId(turnoId));
    }

    /**
     * Eliminar turno asignado
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<MedicShiftDTO> existing = service.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Asignación de turno no encontrada");
        }

        service.delete(id);
        return ResponseEntity.ok("Turno eliminado correctamente");
    }
}
