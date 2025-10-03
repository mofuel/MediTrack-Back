package com.MediTrack.domain.dto;

/**
 * DTO que representa los datos necesarios para registrar a
 * un nuevo usuario
 */

public class RegisterDTO {

    private String nombre;

    private String apellido;

    private String dni;

    private String telefono;

    private String email;

    private String sexo;

    private String password;

    private String confirmPassword;

    //Getters y Setters


    public String getNombre() {return nombre;}

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getApellido() {return apellido;}

    public void setApellido(String apellido) {this.apellido = apellido;}

    public String getDni() {return dni;}

    public void setDni(String dni) {this.dni = dni;}

    public String getTelefono() {return telefono;}

    public void setTelefono(String telefono) {this.telefono = telefono;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getSexo() {return sexo;}

    public void setSexo(String sexo) {this.sexo = sexo;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getConfirmPassword() {return confirmPassword;}

    public void setConfirmPassword(String confirmPassword) {this.confirmPassword = confirmPassword;}
}
