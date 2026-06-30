package com.MediTrack.domain.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class SlotDisponibleDTO {

    private String medicoId;
    private String medicoNombre;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private boolean disponible;

    public String getMedicoId() { return medicoId; }

    public void setMedicoId(String medicoId) { this.medicoId = medicoId; }

    public String getMedicoNombre() { return medicoNombre; }

    public void setMedicoNombre(String medicoNombre) { this.medicoNombre = medicoNombre; }

    public LocalDate getFecha() { return fecha; }

    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }

    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }

    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public boolean isDisponible() { return disponible; }

    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}