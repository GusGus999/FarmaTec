package com.example.farmatec.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venta {
    private int idVenta;
    private LocalDateTime fechaHora;
    private int idSesion;
    private double total;
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
        this.fechaHora = LocalDateTime.now();
    }

    public Venta(int idVenta, LocalDateTime fechaHora, int idSesion, double total) {
        this.idVenta = idVenta;
        this.fechaHora = fechaHora;
        this.idSesion = idSesion;
        this.total = total;
    }

    public double calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        return this.total;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public int getIdSesion() { return idSesion; }
    public void setIdSesion(int idSesion) { this.idSesion = idSesion; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public List<DetalleVenta> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }
}