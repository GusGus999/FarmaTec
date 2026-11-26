package com.example.farmatec.service;

import com.example.farmatec.model.*;
import com.example.farmatec.repository.LoteRepository;
import com.example.farmatec.repository.MedicamentoRepository;
import com.example.farmatec.repository.VentaRepository;
import java.util.List;

public class ServicioVentas {

    private final LoteRepository loteRepo = new LoteRepository();
    private final MedicamentoRepository medRepo = new MedicamentoRepository();
    private final VentaRepository ventaRepo = new VentaRepository();
    private final AlgoritmoFEFO algoritmoFEFO = new AlgoritmoFEFO();
    private final ValidadorCaducidad validador = new ValidadorCaducidad();

    public Lote obtenerLoteMasProximo(int idMedicamento) {
        List<Lote> disponibles = loteRepo.obtenerDisponiblesPorMedicamento(idMedicamento);
        return algoritmoFEFO.ordenarPorCaducidad(disponibles);
    }

    public Lote procesarLecturaPorCodigo(String codigoBarras) {
        Medicamento med = medRepo.obtenerPorCodigo(codigoBarras);
        if (med == null) return null;

        List<Lote> lotes = loteRepo.obtenerDisponiblesPorMedicamento(med.getIdMedicamento());
        Lote loteFEFO = algoritmoFEFO.ordenarPorCaducidad(lotes);

        // Si no hay lotes vigentes pero sí uno caducado en anaquel, lo devolvemos marcado para disparar alerta restrictiva (RF-03)
        if (loteFEFO == null && !lotes.isEmpty()) {
            Lote primerLote = lotes.get(0);
            primerLote.setEstado("Caducado");
            return primerLote;
        }
        return loteFEFO;
    }

    public boolean registrarSalidaInventario(Venta venta, List<DetalleVenta> detalles) {
        if (venta == null || detalles == null || detalles.isEmpty()) {
            return false;
        }
        venta.setDetalles(detalles);
        venta.calcularTotal();
        return ventaRepo.guardar(venta, detalles);
    }

    public List<Descuento> obtenerPromocionesActivas() {
        return loteRepo.obtenerDescuentosActivos();
    }
}