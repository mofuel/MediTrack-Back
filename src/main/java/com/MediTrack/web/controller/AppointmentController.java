package com.MediTrack.web.controller;

import com.MediTrack.domain.dto.AppointmentDTO;
import com.MediTrack.domain.dto.AppointmentViewDTO;
import com.MediTrack.domain.service.AppointmentService;
import com.MediTrack.domain.service.UserService;
import com.MediTrack.persistance.entity.User;
import com.MediTrack.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.MediTrack.domain.dto.SlotDisponibleDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    private ResponseEntity<?> handleError(Exception e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("error", e.getMessage()));
    }

    @GetMapping("/slots")
    public ResponseEntity<?> getSlotsDisponibles(
            @RequestParam String medicoId,
            @RequestParam String fecha) {

        try {
            LocalDate fechaParsed = LocalDate.parse(fecha);
            List<SlotDisponibleDTO> slots = appointmentService.obtenerSlotsDisponibles(medicoId, fechaParsed);
            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            return handleError(e);
        }
    }


    @PostMapping
    public ResponseEntity<?> createAppointment(
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            AppointmentViewDTO saved = appointmentService.saveDTO(dto, email);
            return ResponseEntity.ok(saved);

        } catch (IllegalArgumentException e) {
            // Errores de validación del usuario
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            // Errores de sistema (entidades no encontradas)
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado al crear la cita"));
        }
    }


    // Crear cita como admin
    @PostMapping("/admin")
    public ResponseEntity<?> createAppointmentAsAdmin(
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            boolean esAdmin = appointmentService.validarAdmin(email);
            if (!esAdmin) return ResponseEntity.status(403).build();

            AppointmentViewDTO saved = appointmentService.saveDTOAsAdmin(dto);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllAppointments(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            User user = userService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<AppointmentViewDTO> citas;
            if ("ROLE_ADMIN".equals(user.getRol())) {
                citas = appointmentService.getAllAppointmentsView();
            } else if ("ROLE_MEDICO".equals(user.getRol())) {
                citas = appointmentService.getByMedicoIdView(user.getCodigo());
            } else {
                citas = appointmentService.getByPacienteIdView(user.getCodigo());
            }
            return ResponseEntity.ok(citas);

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable Long id) {
        try {
            Optional<AppointmentViewDTO> cita = appointmentService.getByIdView(id);
            return cita.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return handleError(e);
        }
    }


    @GetMapping("/paciente/{codigoPaciente}")
    public ResponseEntity<?> getByPaciente(
            @PathVariable String codigoPaciente,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            if (!appointmentService.validarPaciente(email, codigoPaciente))
                return ResponseEntity.status(403).build();

            List<AppointmentViewDTO> citas = appointmentService.getByPacienteIdView(codigoPaciente);
            return ResponseEntity.ok(citas);

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @GetMapping("/medico/{codigoMedico}")
    public ResponseEntity<?> getByMedico(
            @PathVariable String codigoMedico,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            if (!appointmentService.validarMedico(email, codigoMedico))
                return ResponseEntity.status(403).build();

            List<AppointmentViewDTO> citas = appointmentService.getByMedicoIdView(codigoMedico);
            return ResponseEntity.ok(citas);

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentDTO dto,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            if (!appointmentService.validarAdmin(email))
                return ResponseEntity.status(403).build();

            Optional<AppointmentViewDTO> updated = appointmentService.updateAppointmentView(id, dto);
            return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(
            @PathVariable Long id,
            @RequestParam String estado) {

        try {
            Optional<AppointmentViewDTO> updated = appointmentService.updateEstadoView(id, estado.toUpperCase());
            return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @GetMapping("/pendientes")
    public ResponseEntity<?> getPendientes(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            User user = userService.buscarPorEmail(email)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            List<AppointmentViewDTO> pendientes;
            if ("ROLE_ADMIN".equals(user.getRol())) {
                pendientes = appointmentService.getByEstadoView("PENDIENTE");
            } else if ("ROLE_MEDICO".equals(user.getRol())) {
                pendientes = appointmentService.getByMedicoIdView(user.getCodigo()).stream()
                        .filter(c -> "PENDIENTE".equals(c.getEstado()))
                        .collect(java.util.stream.Collectors.toList());
            } else {
                pendientes = appointmentService.getByPacienteIdView(user.getCodigo()).stream()
                        .filter(c -> "PENDIENTE".equals(c.getEstado()))
                        .collect(java.util.stream.Collectors.toList());
            }
            return ResponseEntity.ok(pendientes);

        } catch (Exception e) {
            return handleError(e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppointment(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7);
            String email = jwtUtil.getUsernameFromToken(token);

            if (!appointmentService.validarAdmin(email))
                return ResponseEntity.status(403).build();

            boolean deleted = appointmentService.deleteAppointment(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

        } catch (Exception e) {
            return handleError(e);
        }
    }
}
