package com.MediTrack.persistance.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa a los tokens del sistema
 */

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_codigo", length = 10, nullable = false)
    private String userCodigo;   // FK

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();


    //Getters y Setters

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getUserCodigo() {return userCodigo;}

    public void setUserCodigo(String userCodigo) {this.userCodigo = userCodigo;}

    public String getTokenHash() {return tokenHash;}

    public void setTokenHash(String tokenHash) {this.tokenHash = tokenHash;}

    public LocalDateTime getExpiresAt() {return expiresAt;}

    public void setExpiresAt(LocalDateTime expiresAt) {this.expiresAt = expiresAt;}

    public boolean isUsed() {return used;}

    public void setUsed(boolean used) {this.used = used;}

    public LocalDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}
}
