package com.MediTrack.domain.service;

import com.MediTrack.domain.dto.SlotDisponibleDTO;
import com.MediTrack.persistance.crud.AppointmentCrudRepository;
import com.MediTrack.persistance.crud.MedicShiftCrudRepository;
import com.MediTrack.persistance.entity.Appointment;
import com.MediTrack.persistance.entity.ClinicalShifts;
import com.MediTrack.persistance.entity.MedicShift;
import com.MediTrack.persistance.entity.MedicProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DisponibilidadService {

    @Autowired
    private MedicShiftCrudRepository medicShiftCrud;

    @Autowired
    private AppointmentCrudRepository appointmentCrud;

    private static final int SLOT_DURATION = 60;

    public List<SlotDisponibleDTO> obtenerSlotsDisponibles(String medicoCodigo, LocalDate fecha) {
        return obtenerSlotsDisponibles(medicoCodigo, fecha, null);
    }

    public List<SlotDisponibleDTO> obtenerSlotsDisponibles(String medicoCodigo, LocalDate fecha, Long citaExcluirId) {
        DayOfWeek dayOfWeek = fecha.getDayOfWeek();
        short diaSemana = (short) dayOfWeek.getValue();

        List<MedicShift> shifts = medicShiftCrud
                .findByPerfilMedico_CodigoUsuarioAndDiaSemana(medicoCodigo, diaSemana);

        if (shifts.isEmpty()) {
            return List.of();
        }

        MedicProfile perfil = shifts.get(0).getPerfilMedico();
        if (perfil == null || perfil.getUser() == null) {
            return List.of();
        }

        String medicoNombre = perfil.getUser().getNombre()
                + " " + perfil.getUser().getApellido();

        Set<LocalTime> horasOcupadas = appointmentCrud
                .findByMedico_CodigoUsuarioAndFechaCitaOrderByHoraCitaAsc(medicoCodigo, fecha)
                .stream()
                .filter(a -> citaExcluirId == null || !a.getId().equals(citaExcluirId))
                .map(Appointment::getHoraCita)
                .collect(Collectors.toSet());

        List<SlotDisponibleDTO> slots = new ArrayList<>();

        for (MedicShift shift : shifts) {
            ClinicalShifts turno = shift.getTurno();
            LocalTime inicio = turno.getHoraInicio();
            LocalTime fin = turno.getHoraFin();

            LocalTime hora = inicio;
            while (hora.isBefore(fin)) {
                LocalTime horaFinSlot = hora.plusMinutes(SLOT_DURATION);
                if (horaFinSlot.isAfter(fin)) break;

                SlotDisponibleDTO slot = new SlotDisponibleDTO();
                slot.setMedicoId(medicoCodigo);
                slot.setMedicoNombre(medicoNombre);
                slot.setFecha(fecha);
                slot.setHoraInicio(hora);
                slot.setHoraFin(horaFinSlot);
                slot.setDisponible(!horasOcupadas.contains(hora));

                slots.add(slot);
                hora = horaFinSlot;
            }
        }

        return slots;
    }
}
