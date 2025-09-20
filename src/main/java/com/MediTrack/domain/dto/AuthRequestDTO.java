package com.MediTrack.domain.dto;


/**
 * DTO que encapsula las credenciales de inicio de sesión
 * Se envía desde el cliente al backend para solicitar un JWT
 */

public class AuthRequestDTO {

    private String email;
    private String password;

    // Getters y Setters

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
