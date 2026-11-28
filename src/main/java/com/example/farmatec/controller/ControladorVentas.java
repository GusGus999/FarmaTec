package com.example.farmatec.controller;

import com.example.farmatec.model.Descuento;
import com.example.farmatec.model.DetalleVenta;
import com.example.farmatec.model.Lote;
import com.example.farmatec.model.Medicamento;
import com.example.farmatec.model.Venta;
import com.example.farmatec.repository.MedicamentoRepository;
import com.example.farmatec.service.ServicioVentas;
import com.example.farmatec.service.ValidadorCaducidad;
import java.util.List;

public class ControladorVentas {

    private final ServicioVentas servicioVentas = new ServicioVentas();
    private final MedicamentoRepository medRepo = new MedicamentoRepository();
    private final ValidadorCaducidad validador = new ValidadorCaducidad();

    public Lote consultarUbicacionYSugerirFEFO(String nombre) {
        List<Medicamento> encontrados = medRepo.buscarPorNombreOMarca(nombre);
        if (encontrados.isEmpty()) return null;
        return servicioVentas.obtenerLoteMasProximo(encontrados.get(0).getIdMedicamento());
    }

    public Lote procesarLecturaVenta(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            return null;
        }
        return servicioVentas.procesarLecturaPorCodigo(codigoBarras.trim());
    }

    public boolean esLoteAptoParaVenta(Lote lote) {
        return validador.validarVigenciaLote(lote);
    }

    public boolean confirmarCobro(Venta venta, List<DetalleVenta> detalles) {
        return servicioVentas.registrarSalidaInventario(venta, detalles);
    }

    public List<Descuento> obtenerRecordatorioPromociones() {
        return servicioVentas.obtenerPromocionesActivas();
    }
}  