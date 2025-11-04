package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.ClinicalShiftsDTO;
import com.MediTrack.domain.service.ClinicalShiftsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turnos")
public class ClinicalShiftsController {

    @Autowired
    private ClinicalShiftsService service;

    /**
     * Crear un nuevo turno (solo ADMIN)
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> crearTurno(@RequestBody ClinicalShiftsDTO dto) {
        // Verificar si el nombre ya existe
        Optional<ClinicalShiftsDTO> existente = service.findByNombre(dto.getNombre());
        if (existente.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("❌ Ya existe un turno con ese nombre");
        }

        ClinicalShiftsDTO nuevo = service.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    /**
     * Listar todos los turnos (ADMIN, MEDICO o PACIENTE)
     */
    // @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'PACIENTE')")
    @GetMapping
    public ResponseEntity<List<ClinicalShiftsDTO>> listarTurnos() {
        List<ClinicalShiftsDTO> turnos = service.findAll();
        return ResponseEntity.ok(turnos);
    }

    /**
     * Buscar un turno por ID
     */
    // @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        Optional<ClinicalShiftsDTO> turno = service.findById(id);
        return turno.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("❌ Turno no encontrado"));
    }

    /**
     * Eliminar un turno (solo ADMIN)
     */
    // @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarTurno(@PathVariable Long id) {
        Optional<ClinicalShiftsDTO> turno = service.findById(id);
        if (turno.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("❌ No se encontró el turno con ese ID");
        }

        service.deleteById(id);
        return ResponseEntity.ok("✅ Turno eliminado correctamente");
    }
}
