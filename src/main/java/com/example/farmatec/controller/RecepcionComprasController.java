package com.example.farmatec.controller;

import com.example.farmatec.model.Lote;
import com.example.farmatec.model.Medicamento;
import com.example.farmatec.repository.MedicamentoRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RecepcionComprasController {

    @FXML private TextField txtCodigoBarras;
    @FXML private Label lblEstadoProducto;

    // Campos del Medicamento
    @FXML private TextField txtMarca;
    @FXML private TextField txtNombre;
    @FXML private TextField txtConcentracion;
    @FXML private TextField txtPresentacion;
    @FXML private TextField txtPrecioCompra;
    @FXML private TextField txtPrecioVenta;
    @FXML private TextField txtUbicacion;

    // Campos del Lote (RF-01)
    @FXML private TextField txtNumeroLote;
    @FXML private DatePicker dpFechaCaducidad;
    @FXML private TextField txtCantidad;
    @FXML private Label lblSugerenciaDemanda;

    // Tabla de ingresos de la sesión
    @FXML private TableView<Lote> tblIngresos;
    @FXML private TableColumn<Lote, String> colCodigo;
    @FXML private TableColumn<Lote, String> colMedicamento;
    @FXML private TableColumn<Lote, String> colLote;
    @FXML private TableColumn<Lote, String> colCaducidad;
    @FXML private TableColumn<Lote, String> colCantidad;
    @FXML private TableColumn<Lote, String> colUbicacion;

    private final ControladorInventario controladorInventario = new ControladorInventario();
    private final MedicamentoRepository medicamentoRepo = new MedicamentoRepository();
    private final ObservableList<Lote> historialSesion = FXCollections.observableArrayList();

    private Medicamento medicamentoActual;
    private String ultimoCodigoBuscado = "";

    @FXML
    public void initialize() {
        if (com.example.farmatec.model.SesionManager.getSesionActual() != null) {
            controladorInventario.setIdSesionActiva(
                    com.example.farmatec.model.SesionManager.getSesionActual().getIdSesion()
            );
        }if (com.example.farmatec.model.SesionManager.getSesionActual() != null) {
            controladorInventario.setIdSesionActiva(
                    com.example.farmatec.model.SesionManager.getSesionActual().getIdSesion()
            );
        }
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(ultimoCodigoBuscado));
        colMedicamento.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombreMedicamento()));
        colLote.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNumeroLote()));
        colCaducidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getFechaCaducidad().toString()));
        colCantidad.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCantidad() + " pz"));
        colUbicacion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUbicacionMedicamento()));

        tblIngresos.setItems(historialSesion);
    }

    @FXML
    public void capturarCodigoIngreso() {
        String codigo = txtCodigoBarras.getText() == null ? "" : txtCodigoBarras.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Código requerido", "Escanee o escriba un código de barras válido.");
            return;
        }

        medicamentoActual = medicamentoRepo.obtenerPorCodigo(codigo);
        if (medicamentoActual != null) {
            // El producto ya existe en catálogo: cargar datos y bloquear ficha maestra
            lblEstadoProducto.setText("✓ Producto en Catálogo (Solo sumar lote)");
            lblEstadoProducto.setStyle("-fx-background-color: #89A194; -fx-text-fill: #322938; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 8;");

            txtMarca.setText(medicamentoActual.getMarca());
            txtNombre.setText(medicamentoActual.getNombre());
            txtConcentracion.setText(medicamentoActual.getConcentracion());
            txtPresentacion.setText(medicamentoActual.getPresentacion());
            txtPrecioCompra.setText(String.valueOf(medicamentoActual.getPrecioCompra()));
            txtPrecioVenta.setText(String.valueOf(medicamentoActual.getPrecioVenta()));
            txtUbicacion.setText(medicamentoActual.getUbicacion());

            setCamposMedicamentoEditables(false);
            lblSugerenciaDemanda.setText("Demanda: " + controladorInventario.verificarDemandaCompra(medicamentoActual.getIdMedicamento()));
            txtNumeroLote.requestFocus();
        } else {
            // Producto nuevo: habilitar formulario de alta
            mostrarFormularioProductoNuevo();
        }
    }

    public void mostrarFormularioProductoNuevo() {
        medicamentoActual = null;
        lblEstadoProducto.setText("⚠ Producto Nuevo: Complete ficha maestra");
        lblEstadoProducto.setStyle("-fx-background-color: #CC883A; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 8;");

        txtMarca.clear();
        txtNombre.clear();
        txtConcentracion.clear();
        txtPresentacion.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        txtUbicacion.clear();

        setCamposMedicamentoEditables(true);
        lblSugerenciaDemanda.setText("Producto sin registro previo. Complete los datos del medicamento y del lote.");
        txtMarca.requestFocus();
    }

    @FXML
    public void capturarDatosLote() {
        String codigo = txtCodigoBarras.getText() == null ? "" : txtCodigoBarras.getText().trim();
        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Falta código", "Ingrese el código de barras del producto.");
            return;
        }

        try {
            Medicamento med = (medicamentoActual != null) ? medicamentoActual : new Medicamento(
                    0,
                    codigo,
                    txtMarca.getText().trim(),
                    txtNombre.getText().trim(),
                    txtConcentracion.getText().trim(),
                    txtPresentacion.getText().trim(),
                    Double.parseDouble(txtPrecioCompra.getText().trim()),
                    Double.parseDouble(txtPrecioVenta.getText().trim()),
                    txtUbicacion.getText().trim()
            );

            Lote lote = new Lote();
            lote.setNumeroLote(txtNumeroLote.getText() == null ? "" : txtNumeroLote.getText().trim());
            lote.setFechaCaducidad(dpFechaCaducidad.getValue());
            lote.setCantidad(Integer.parseInt(txtCantidad.getText().trim()));

            boolean guardado = controladorInventario.registrarIngresoCompra(med, lote);
            if (guardado) {
                ultimoCodigoBuscado = codigo;
                lote.setNombreMedicamento(med.getNombre() + " " + med.getConcentracion());
                lote.setUbicacionMedicamento(med.getUbicacion());
                historialSesion.add(0, lote);

                mostrarAlerta(Alert.AlertType.INFORMATION, "Ingreso Registrado",
                        "Lote " + lote.getNumeroLote() + " registrado correctamente en MySQL (Log: ENTRADA_COMPRA).");
                cancelarCaptura();
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Validación Rechazada (RF-01)",
                        "No se pudo guardar: verifique que todos los campos estén completos y que la fecha de caducidad sea futura.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato Inválido",
                    "Precio de compra, precio de venta y cantidad deben ser valores numéricos válidos.");
        }
    }

    @FXML
    public void cancelarCaptura() {
        medicamentoActual = null;
        txtCodigoBarras.clear();
        txtMarca.clear();
        txtNombre.clear();
        txtConcentracion.clear();
        txtPresentacion.clear();
        txtPrecioCompra.clear();
        txtPrecioVenta.clear();
        txtUbicacion.clear();
        txtNumeroLote.clear();
        dpFechaCaducidad.setValue(null);
        txtCantidad.clear();
        setCamposMedicamentoEditables(true);
        lblEstadoProducto.setText("Esperando lectura de código...");
        lblEstadoProducto.setStyle("-fx-background-color: #CC883A; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; -fx-padding: 6 14; -fx-background-radius: 8;");
        lblSugerenciaDemanda.setText("Evaluación de demanda: Verifique el código para revisar stock actual.");
        txtCodigoBarras.requestFocus();
    }

    private void setCamposMedicamentoEditables(boolean editable) {
        txtMarca.setEditable(editable);
        txtNombre.setEditable(editable);
        txtConcentracion.setEditable(editable);
        txtPresentacion.setEditable(editable);
        txtPrecioCompra.setEditable(editable);
        txtPrecioVenta.setEditable(editable);
        txtUbicacion.setEditable(editable);
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void volverAlMenu() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    com.example.farmatec.App.class.getResource("VistaMenuPrincipal.fxml")
            );
            javafx.stage.Stage stage = (javafx.stage.Stage) txtCodigoBarras.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(loader.load(), 1040, 660));
            stage.setTitle("FarmaTec - Menú Principal");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}