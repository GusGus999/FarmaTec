package com.example.farmatec.model;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idLote;
    private int cantidad;
    private double precioUnitario;
     
    private String nombreMedicamento;
    private String numeroLote;

    public DetalleVenta() {}

    public DetalleVenta(int idDetalle, int idVenta, int idLote, int cantidad, double precioUnitario) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idLote = idLote;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    public int getIdDetalle() { return idDetalle; }
    public void setIdDetalle(int idDetalle) { this.idDetalle = idDetalle; }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public int getIdLote() { return idLote; }
    public void setIdLote(int idLote) { this.idLote = idLote; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public String getNombreMedicamento() { return nombreMedicamento; }
    public void setNombreMedicamento(String nombreMedicamento) { this.nombreMedicamento = nombreMedicamento; }

    public String getNumeroLote() { return numeroLote; }
    public void setNumeroLote(String numeroLote) { this.numeroLote = numeroLote; }
}