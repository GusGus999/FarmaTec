//Se encarga únicamente de obtener o guardar datos
package com.example.farmatec.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {

    private final List<Producto> productos = new ArrayList<>();

    public ProductoRepository() {
        productos.add(new Producto(1, "Teclado", 450.0));
        productos.add(new Producto(2, "Mouse", 250.0));
        productos.add(new Producto(3, "Monitor", 3200.0));
    }

    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos);
    }

    public Optional<Producto> buscarPorId(int id) {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public void guardar(Producto producto) {
        productos.add(producto);
    }
}