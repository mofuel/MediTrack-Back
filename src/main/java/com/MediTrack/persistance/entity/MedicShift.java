package com.MediTrack.persistance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medico_turno")
public class MedicShift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dia_semana", nullable = false)
    private short diaSemana; // 1 = Lunes ... 7 = Domingo

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private MedicProfile perfilMedico;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private ClinicalShifts turno;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(short diaSemana) {
        this.diaSemana = diaSemana;
    }

    public MedicProfile getPerfilMedico() {
        return perfilMedico;
    }

    public void setPerfilMedico(MedicProfile perfilMedico) {
        this.perfilMedico = perfilMedico;
    }

    public ClinicalShifts getTurno() {
        return turno;
    }

    public void setTurno(ClinicalShifts turno) {
        this.turno = turno;
    }
}
