package com.example.farmatec.model;

import java.time.LocalDateTime;

public class Descuento {
    private int idDescuento;
    private int idLote;
    private double porcentaje;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String tipoDescuento; // "Semaforo Amarillo" o "Semaforo Rojo"
    private int idSesion;

    private String descripcionLote;

    public Descuento() {}

    public Descuento(int idDescuento, int idLote, double porcentaje, LocalDateTime fechaInicio,
                     LocalDateTime fechaFin, String tipoDescuento, int idSesion) {
        this.idDescuento = idDescuento;
        this.idLote = idLote;
        this.porcentaje = porcentaje;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipoDescuento = tipoDescuento;
        this.idSesion = idSesion;
    }

    public double aplicarDescuento(double precioBase) {
        return precioBase * (1.0 - (porcentaje / 100.0));
    }

    public int getIdDescuento() { return idDescuento; }
    public void setIdDescuento(int idDescuento) { this.idDescuento = idDescuento; }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }

    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }

    public String getTipoDescuento() { return tipoDescuento; }
    public void setTipoDescuento(String tipoDescuento) { this.tipoDescuento = tipoDescuento; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public String getDescripcionLote() { return descripcionLote; }
    public void setDescripcionLote(String descripcionLote) { this.descripcionLote = descripcionLote; }
}