package com.example.farmatec.service;

import com.example.farmatec.model.Lote;
import java.time.LocalDate;

public class ValidadorCaducidad {

    public boolean validarVigenciaLote(Lote lote) {
        if (lote == null || lote.getCantidad() <= 0) {
            return false;
        }
        if ("Caducado".equalsIgnoreCase(lote.getEstado()) || "Agotado".equalsIgnoreCase(lote.getEstado())) {
            return false;
        }
        return !estaCaducado(lote.getFechaCaducidad());
    }

    public boolean estaCaducado(LocalDate fechaCaducidad) {
        return fechaCaducidad == null || !fechaCaducidad.isAfter(LocalDate.now());
    }
}