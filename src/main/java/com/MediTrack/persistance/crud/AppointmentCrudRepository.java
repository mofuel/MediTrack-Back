package com.MediTrack.persistance.crud;

import com.MediTrack.persistance.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentCrudRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPaciente_Codigo(String codigoPaciente);

    List<Appointment> findByMedico_Id(Long medicoId);

    List<Appointment> findByMedico_CodigoUsuario(String codigoUsuario);

    List<Appointment> findByEstado(String estado);

    List<Appointment> findByMedico_CodigoUsuarioAndFechaCitaOrderByHoraCitaAsc(
            String codigoUsuario, LocalDate fechaCita);

    boolean existsByPaciente_CodigoAndFechaCitaAndHoraCita(
            String pacienteCodigo, LocalDate fecha, LocalTime hora);

    boolean existsByMedico_CodigoUsuarioAndFechaCitaAndHoraCita(
            String medicoCodigo, LocalDate fecha, LocalTime hora);
}
