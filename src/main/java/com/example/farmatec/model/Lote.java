package com.example.farmatec.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Lote {
    private int idLote;
    private int idMedicamento;
    private String numeroLote;
    private LocalDate fechaCaducidad;
    private int cantidad;
    private LocalDateTime fechaIngreso;
    private String estado; // "Vigente", "Proximo a vencer", "Caducado", "Agotado"


    private String nombreMedicamento;
    private String ubicacionMedicamento;
    private double precioVentaBase;

    public Lote() {}

    public Lote(int idLote, int idMedicamento, String numeroLote, LocalDate fechaCaducidad,
                int cantidad, LocalDateTime fechaIngreso, String estado) {
        this.idLote = idLote;
        this.idMedicamento = idMedicamento;
        this.numeroLote = numeroLote;
        this.fechaCaducidad = fechaCaducidad;
        this.cantidad = cantidad;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
    }

    public boolean estaCaducado() {
        return fechaCaducidad != null && !fechaCaducidad.isAfter(LocalDate.now());
    }

    public long getDiasRestantes() {
        if (fechaCaducidad == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaCaducidad);
    }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public LocalDateTime getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDateTime fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }

    public String getUbicacionMedicamento() { return ubicacionMedicamento; }
    public void setUbicacionMedicamento(String ubicacionMedicamento) { this.ubicacionMedicamento = ubicacionMedicamento; }

    public double getPrecioVentaBase() { return precioVentaBase; }
    public void setPrecioVentaBase(double precioVentaBase) { this.precioVentaBase = precioVentaBase; }
}