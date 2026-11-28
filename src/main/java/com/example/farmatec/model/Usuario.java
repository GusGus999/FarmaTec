package com.example.farmatec.model;

public class Usuario {
    private int idUsuario;
    private String nombre;
    private String rol; // "Administrador" o "Despachador"
    private String credencial;

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String rol, String credencial) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
        this.credencial = credencial;
    }

    public boolean iniciarSesion(String credencialIngresada) {
        return this.credencial != null && this.credencial.equals(credencialIngresada);
    }

    public boolean esAdministrador() {
        return "Administrador".equalsIgnoreCase(this.rol);
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getCredencial() { return credencial; }
    public void setCredencial(String credencial) { this.credencial = credencial; }
}