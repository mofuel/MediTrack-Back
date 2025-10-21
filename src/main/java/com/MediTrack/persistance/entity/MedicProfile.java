package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "perfil_medico")
public class MedicProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_usuario", length = 10, nullable = false, unique = true)
    private String codigoUsuario; // FK hacia User.codigo

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    // Relaciones
    @OneToOne
    @JoinColumn(name = "codigo_usuario", referencedColumnName = "codigo", insertable = false, updatable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "perfil_medico_especialidad",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    private Set<Specialty> especialidades;

    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getCodigoUsuario() {return codigoUsuario;}

    public void setCodigoUsuario(String codigoUsuario) {this.codigoUsuario = codigoUsuario;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public Set<Specialty> getEspecialidades() { return especialidades; }

    public void setEspecialidades(Set<Specialty> especialidades) { this.especialidades = especialidades; }
}
