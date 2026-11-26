// El controlador recibe eventos de la vista
package com.example.farmatec.controller;

import com.example.farmatec.service.ProductoService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ProductoController {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoPrecio;

    @FXML
    private ListView<Producto> listaProductos;

    @FXML
    private Label mensaje;

    private final ProductoService productoService;

    public ProductoController() {
        this.productoService = new ProductoService(new com.example.farmatec.repository.ProductoRepository());
    }

    @FXML
    public void initialize() {
        cargarProductos();
    }

    @FXML
    public void guardarProducto() {
        try {
            String nombre = campoNombre.getText();
            double precio = Double.parseDouble(campoPrecio.getText());

            int nuevoId = listaProductos.getItems().size() + 1;

            productoService.registrarProducto(nuevoId, nombre, precio);
            cargarProductos();

            campoNombre.clear();
            campoPrecio.clear();
            mensaje.setText("Producto guardado correctamente.");

        } catch (NumberFormatException e) {
            mensaje.setText("El precio debe ser un número válido.");
        } catch (IllegalArgumentException e) {
            mensaje.setText(e.getMessage());
        }
    }

    private void cargarProductos() {
        listaProductos.getItems().setAll(productoService.obtenerProductos());
    }
}