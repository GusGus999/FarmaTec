// Contiene las reglas de negocio y actúa como intermediaria entre la interfaz y el repositorio.
package com.example.farmatec.service;

import com.example.farmatec.model.Producto;
import com.example.farmatec.repository.ProductoRepository;

import java.util.List;

public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<Producto> obtenerProductos() {
        return repository.obtenerTodos();
    }

    public void registrarProducto(int id, String nombre, double precio) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }

        repository.guardar(new Producto(id, nombre, precio));
    }
}