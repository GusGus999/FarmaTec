package com.example.farmatec.controller;

import com.example.farmatec.model.Lote;
import com.example.farmatec.model.Medicamento;
import com.example.farmatec.service.ServicioInventario;
import java.time.LocalDateTime;
import java.util.List;

public class ControladorInventario {

    private final ServicioInventario servicioInventario = new ServicioInventario();
    private int idSesionActiva = 1; // Se actualiza al iniciar sesión

    public void setIdSesionActiva(int idSesionActiva) {
        this.idSesionActiva = idSesionActiva;
    }

    public List<Medicamento> consultarProductos(String criterio) {
        return servicioInventario.consultarProductos(criterio);
    }

    public boolean actualizarProducto(Medicamento med) {
        return servicioInventario.actualizarProducto(med);
    }

    public boolean registrarIngresoCompra(Medicamento med, Lote lote) {
        if (lote == null || lote.getNumeroLote() == null || lote.getCantidad() <= 0) {
            return false;
        }
        return servicioInventario.registrarIngresoCompra(med, lote, idSesionActiva);
    }

    public boolean aplicarPromocion(int idLote, double porcentaje, LocalDateTime inicio, LocalDateTime fin) {
        return servicioInventario.aplicarPromocion(idLote, porcentaje, inicio, fin, idSesionActiva);
    }

    public List<Lote> obtenerLotesSemaforizados() {
        return servicioInventario.evaluarCaducidadesDiarias();
    }

    public double sugerirPorcentajeDescuento(Lote lote) {
        return servicioInventario.calcularDescuentoSemaforo(lote);
    }

    public String verificarDemandaCompra(int idMedicamento) {
        return servicioInventario.evaluarNecesidadCompra(idMedicamento);
    }

    public boolean registrarBajaMerma(int idLote, int cantidad) {
        return servicioInventario.darDeBajaMerma(idLote, cantidad, idSesionActiva);
    }
} 