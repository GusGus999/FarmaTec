package com.example.farmatec.controller;

import com.example.farmatec.App;
import com.example.farmatec.model.Sesion;
import com.example.farmatec.model.SesionManager;
import com.example.farmatec.repository.UsuarioRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuPrincipalController {

    @FXML private Label lblNombreUsuario;
    @FXML private Label lblRolUsuario;
    @FXML private Label lblPermisoCu03;
    @FXML private Button btnAbrirCu03;

    @FXML
    public void initialize() {
        Sesion sesion = SesionManager.getSesionActual();
        if (sesion != null && sesion.getUsuario() != null) {
            lblNombreUsuario.setText(sesion.getUsuario().getNombre());
            lblRolUsuario.setText("Rol: " + sesion.getUsuario().getRol());
        }
    }

    @FXML
    public void abrirRecepcionCompras() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("VistaRecepcionCompras.fxml"));
            Stage stage = (Stage) btnAbrirCu03.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1080, 700));
            stage.setTitle("FarmaTec - CU03 Recepción de Compras");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarModuloEnConstruccion() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Módulo en Construcción");
        alert.setHeaderText("Interfaz aún no integrada en el proyecto");
        alert.setContentText("Este espacio está reservado para que el compañero responsable conecte su archivo .fxml y su controlador cuando lo tenga listo.");
        alert.showAndWait();
    }

    @FXML
    public void cerrarSesion() {
        Sesion sesion = SesionManager.getSesionActual();
        if (sesion != null) {
            new UsuarioRepository().cerrarSesionBD(sesion);
            SesionManager.limpiar();
        }
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("VistaLogin.fxml"));
            Stage stage = (Stage) btnAbrirCu03.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 860, 500));
            stage.setTitle("FarmaTec - Iniciar Sesión");
            stage.centerOnScreen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}