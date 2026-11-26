package com.example.farmatec.service;

import com.example.farmatec.model.*;
import com.example.farmatec.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ServicioInventario {

    private final MedicamentoRepository medRepo = new MedicamentoRepository();
    private final LoteRepository loteRepo = new LoteRepository();
    private final VentaRepository ventaRepo = new VentaRepository();
    private final LogRepository logRepo = new LogRepository();
    private final ValidadorCaducidad validador = new ValidadorCaducidad();
    private final AlgoritmoFEFO algoritmoFEFO = new AlgoritmoFEFO();

    public List<Lote> evaluarCaducidadesDiarias() {
        List<Lote> lotes = loteRepo.obtenerLotesConStock();
        for (Lote l : lotes) {
            long dias = l.getDiasRestantes();
            if (dias <= 0 && !"Caducado".equalsIgnoreCase(l.getEstado())) {
                l.setEstado("Caducado");
                loteRepo.actualizarEstado(l.getIdLote(), "Caducado", false);
            } else if (dias > 0 && dias <= 60 && "Vigente".equalsIgnoreCase(l.getEstado())) {
                l.setEstado("Proximo a vencer");
                loteRepo.actualizarEstado(l.getIdLote(), "Proximo a vencer", false);
            }
        }
        return algoritmoFEFO.ordenarListaPorCaducidad(lotes);
    }

    public double calcularDescuentoSemaforo(Lote lote) {
        long dias = lote.getDiasRestantes();
        if (dias <= 0) return 100.0; // Merma obligatoria
        if (dias <= 30) return 40.0; // Semáforo Rojo
        if (dias <= 60) return 20.0; // Semáforo Amarillo
        if (dias <= 90) return 10.0; // Semáforo Verde preventivo
        return 0.0;
    }

    public double calcularDescuentoDemanda(int idMedicamento) {
        int vendidosMes = ventaRepo.obtenerTotalVendido(idMedicamento, LocalDate.now().minusDays(30), LocalDate.now());
        if (vendidosMes < 5) return 10.0;
        if (vendidosMes < 15) return 5.0;
        return 0.0;
    }

    public String evaluarNecesidadCompra(int idMedicamento) {
        List<Lote> lotes = loteRepo.obtenerDisponiblesPorMedicamento(idMedicamento);
        int stockVigente = lotes.stream()
                .filter(validador::validarVigenciaLote)
                .mapToInt(Lote::getCantidad)
                .sum();
        int vendidosMes = ventaRepo.obtenerTotalVendido(idMedicamento, LocalDate.now().minusDays(30), LocalDate.now());

        if (stockVigente == 0) {
            return "URGENTE: Sin existencias vigentes en tienda.";
        } else if (stockVigente <= vendidosMes) {
            return "RECOMENDADO: Stock bajo (" + stockVigente + " pz) respecto a demanda mensual (" + vendidosMes + " pz).";
        }
        return "SUFICIENTE: Inventario saludable (" + stockVigente + " pz disponibles).";
    }

    public List<Medicamento> consultarProductos(String criterio) {
        return medRepo.buscarPorNombreOMarca(criterio);
    }

    public boolean actualizarProducto(Medicamento med) {
        if (med == null || med.getIdMedicamento() <= 0 || med.getPrecioVenta() <= 0) {
            return false;
        }
        return medRepo.actualizar(med);
    }

    public boolean registrarIngresoCompra(Medicamento med, Lote lote, int idSesion) {
        if (med == null || lote == null || lote.getCantidad() <= 0 || lote.getNumeroLote() == null || lote.getNumeroLote().isBlank()) {
            return false;
        }
        if (validador.estaCaducado(lote.getFechaCaducidad())) {
            return false;
        }

        Medicamento existente = medRepo.obtenerPorCodigo(med.getCodigoBarras());
        if (existente == null) {
            if (!medRepo.guardar(med)) return false;
            lote.setIdMedicamento(med.getIdMedicamento());
        } else {
            lote.setIdMedicamento(existente.getIdMedicamento());
        }

        lote.setEstado(lote.getDiasRestantes() <= 60 ? "Proximo a vencer" : "Vigente");
        if (loteRepo.guardar(lote)) {
            logRepo.registrarLog(new RegistroTransaccion("ENTRADA_COMPRA", idSesion, lote.getIdLote(), lote.getCantidad()));
            return true;
        }
        return false;
    }

    public boolean aplicarPromocion(int idLote, double porcentaje, LocalDateTime inicio, LocalDateTime fin, int idSesion) {
        if (porcentaje <= 0 || porcentaje >= 100 || fin.isBefore(inicio)) {
            return false;
        }
        String tipo = porcentaje >= 30 ? "Semaforo Rojo" : "Semaforo Amarillo";
        Descuento desc = new Descuento(0, idLote, porcentaje, inicio, fin, tipo, idSesion);
        if (loteRepo.guardarDescuento(desc)) {
            logRepo.registrarLog(new RegistroTransaccion("DESCUENTO", idSesion, idLote, 1));
            return true;
        }
        return false;
    }

    public boolean darDeBajaMerma(int idLote, int cantidadRetirada, int idSesion) {
        if (loteRepo.actualizarEstado(idLote, "Caducado", true)) {
            logRepo.registrarLog(new RegistroTransaccion("BAJA_MERMA", idSesion, idLote, cantidadRetirada));
            return true;
        }
        return false;
    }
}