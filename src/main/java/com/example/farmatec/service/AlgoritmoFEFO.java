package com.example.farmatec.service;

import com.example.farmatec.model.Lote;
import java.util.Comparator;
import java.util.List;

public class AlgoritmoFEFO {

    private final ValidadorCaducidad validador = new ValidadorCaducidad();

    public Lote ordenarPorCaducidad(List<Lote> lotes) {
        if (lotes == null || lotes.isEmpty()) {
            return null;
        }
        return lotes.stream()
                .filter(validador::validarVigenciaLote)
                .min(Comparator.comparing(Lote::getFechaCaducidad))
                .orElse(null);
    }

    public List<Lote> ordenarListaPorCaducidad(List<Lote> lotes) {
        return lotes.stream()
                .sorted(Comparator.comparing(Lote::getFechaCaducidad))
                .toList();
    }
}