package com.example.farmatec.model;

import java.time.LocalDateTime;

public class Sesion {
    private int idSesion;
    private int idUsuario;
    private LocalDateTime inicio;
    private LocalDateTime fin;
    private Usuario usuario;

    public Sesion() {}

    public Sesion(int idSesion, int idUsuario, LocalDateTime inicio, LocalDateTime fin) {
        this.idSesion = idSesion;
        this.idUsuario = idUsuario;
        this.inicio = inicio;
        this.fin = fin;
    }

    public void cerrarSesion() { 
        this.fin = LocalDateTime.now();
    }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime inicio) { this.inicio = inicio; }

    public LocalDateTime getFin() { return fin; }
    public void setFin(LocalDateTime fin) { this.fin = fin; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}