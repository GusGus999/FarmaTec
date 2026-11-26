package com.example.farmatec.model;

public class Medicamento {
    private int idMedicamento;
    private String codigoBarras;
    private String marca;
    private String nombre;
    private String concentracion;
    private String presentacion;
    private double precioCompra;
    private double precioVenta;
    private String ubicacion;

    public Medicamento() {}

    public Medicamento(int idMedicamento, String codigoBarras, String marca, String nombre,
                       String concentracion, String presentacion, double precioCompra,
                       double precioVenta, String ubicacion) {
        this.idMedicamento = idMedicamento;
        this.codigoBarras = codigoBarras;
        this.marca = marca;
        this.nombre = nombre;
        this.concentracion = concentracion;
        this.presentacion = presentacion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.ubicacion = ubicacion;
    }

    public int getIdMedicamento() { return idMedicamento; }
    public void setIdMedicamento(int idMedicamento) { this.idMedicamento = idMedicamento; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getConcentracion() { return concentracion; }
    public void setConcentracion(String concentracion) { this.concentracion = concentracion; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) { this.precioCompra = precioCompra; }

    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    @Override
    public String toString() {
        return nombre + " " + concentracion + " (" + marca + ")";
    }
}