package com.example.farmatec.controller;

import com.example.farmatec.App;
import com.example.farmatec.model.Sesion;
import com.example.farmatec.model.SesionManager;
import com.example.farmatec.repository.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private ComboBox<String> cmbUsuario;
    @FXML private PasswordField txtCredencial;
    @FXML private Label lblMensajeError;

    private final UsuarioRepository usuarioRepo = new UsuarioRepository();

    @FXML
    public void initialize() {
        cmbUsuario.getItems().addAll(
                "Angel Gustavo Guzman",
                "Francisco Clemente Hernandez",
                "Jose Samuel Irineo",
                "Perla Rubi Acosta",
                "Leticia Velazquez"
        );
        cmbUsuario.setValue("Jose Samuel Irineo");
        txtCredencial.setText("sam123");
    }

    @FXML
    public void actualizarSugerenciaPass() {
        lblMensajeError.setText("");
        String seleccionado = cmbUsuario.getValue();
        if ("Angel Gustavo Guzman".equals(seleccionado)) txtCredencial.setText("gus123");
        else if ("Francisco Clemente Hernandez".equals(seleccionado)) txtCredencial.setText("admin123");
        else if ("Jose Samuel Irineo".equals(seleccionado)) txtCredencial.setText("sam123");
        else if ("Perla Rubi Acosta".equals(seleccionado)) txtCredencial.setText("perla123");
        else if ("Leticia Velazquez".equals(seleccionado)) txtCredencial.setText("lety123");
    }

    @FXML
    public void iniciarSesion() {
        String nombre = cmbUsuario.getValue();
        String pass = txtCredencial.getText() == null ? "" : txtCredencial.getText().trim();

        if (nombre == null || pass.isEmpty()) {
            lblMensajeError.setText("Ingrese su usuario y contraseña.");
            return;
        }

        Sesion sesion = usuarioRepo.autenticarYAbrirSesion(nombre, pass);
        if (sesion != null) {
            SesionManager.setSesionActual(sesion);
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("VistaMenuPrincipal.fxml"));
                Stage stage = (Stage) cmbUsuario.getScene().getWindow();
                stage.setScene(new Scene(loader.load(), 1040, 660));
                stage.setTitle("FarmaTec - Menú Principal");
                stage.centerOnScreen();
            } catch (Exception e) {
                lblMensajeError.setText("Error al cargar el menú principal: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            lblMensajeError.setText("⛔ Credencial incorrecta o sin conexión a MySQL.");
        }
    }
}