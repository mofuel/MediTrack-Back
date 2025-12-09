package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentCrudRepository extends JpaRepository<Appointment, Long> {

    // Buscar todas las citas de un paciente por su codigo
    List<Appointment> findByPaciente_Codigo(String codigoPaciente);

    // Buscar todas las citas de un médico por id
    List<Appointment> findByMedico_Id(Long medicoId);

    List<Appointment> findByMedico_CodigoUsuario(String codigoUsuario);

    // Buscar por estado (PENDIENTE, ACEPTADA, RECHAZADA)
    List<Appointment> findByEstado(String estado);

    boolean existsByPaciente_CodigoAndFechaCitaAndHoraCita(
            String pacienteCodigo,
            LocalDate fecha,
            LocalTime hora
    );

    boolean existsByMedico_CodigoUsuarioAndFechaCitaAndHoraCita(
            String medicoCodigo,
            LocalDate fecha,
            LocalTime hora
    );


}
