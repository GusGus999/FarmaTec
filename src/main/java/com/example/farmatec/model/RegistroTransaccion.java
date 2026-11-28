package com.example.farmatec.model;

import java.time.LocalDateTime;

public class RegistroTransaccion {
    private long idRegistro;
    private String tipo; // "ENTRADA_COMPRA", "VENTA", "DESCUENTO", "BAJA_MERMA" 
    private LocalDateTime fechaHora;
    private int idSesion;
    private int idLote;
    private int cantidad;

    public RegistroTransaccion() {
        this.fechaHora = LocalDateTime.now();
    }

    public RegistroTransaccion(String tipo, int idSesion, int idLote, int cantidad) {
        this.tipo = tipo;
        this.fechaHora = LocalDateTime.now();
        this.idSesion = idSesion;
        this.idLote = idLote;
        this.cantidad = cantidad;
    }

    public String registrarLog() {
        return String.format("[%s] %s | Lote ID: %d | Cant: %d | Sesión: %d",
                fechaHora, tipo, idLote, cantidad, idSesion);
    }

    public long getIdRegistro() { return idRegistro; }
    public void setIdRegistro(long idRegistro) { this.idRegistro = idRegistro; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}