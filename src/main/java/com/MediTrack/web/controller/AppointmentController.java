package com.MediTrack.web.controller;

import com.MediTrack.domain.service.AppointmentService;
import com.MediTrack.persistance.entity.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Crear una nueva cita
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        appointment.setEstado("PENDIENTE"); // siempre inicia como pendiente
        Appointment saved = appointmentService.save(appointment);
        return ResponseEntity.ok(saved);
    }

    // Listar todas las citas
    @GetMapping
    public ResponseEntity<List<Appointment>> getAll() {
        return ResponseEntity.ok(appointmentService.getByEstado("PENDIENTE"));
    }

    // Listar citas por paciente
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<Appointment>> getByPaciente(@PathVariable String pacienteId) {
        return ResponseEntity.ok(appointmentService.getByPacienteId(pacienteId));
    }

    // Listar citas por médico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Appointment>> getByMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(appointmentService.getByMedicoId(medicoId));
    }

    // Cambiar estado de la cita (aceptar/rechazar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Appointment> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado
    ) {
        Optional<Appointment> updated = appointmentService.updateEstado(id, estado.toUpperCase());
        return updated
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Obtener citas pendientes para notificaciones
    @GetMapping("/pendientes")
    public ResponseEntity<List<Appointment>> getPendientes() {
        List<Appointment> pendientes = appointmentService.getByEstado("PENDIENTE");
        return ResponseEntity.ok(pendientes);
    }
}