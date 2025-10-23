package com.MediTrack.domain.dto;

public class MedicShiftDTO {

    private Long id;
    private short diaSemana;
    private Long perfilId;
    private Long turnoId;
    private String nombreTurno; // opcional para mostrar
    private String nombreMedico; // opcional para mostrar

    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public short getDiaSemana() {return diaSemana;}

    public void setDiaSemana(short diaSemana) {this.diaSemana = diaSemana;}

    public Long getPerfilId() {return perfilId;}

    public void setPerfilId(Long perfilId) {this.perfilId = perfilId;}

    public Long getTurnoId() {return turnoId;}

    public void setTurnoId(Long turnoId) {this.turnoId = turnoId;}

    public String getNombreTurno() {return nombreTurno;}

    public void setNombreTurno(String nombreTurno) {this.nombreTurno = nombreTurno;}

    public String getNombreMedico() {return nombreMedico;}

    public void setNombreMedico(String nombreMedico) {this.nombreMedico = nombreMedico;}
}
