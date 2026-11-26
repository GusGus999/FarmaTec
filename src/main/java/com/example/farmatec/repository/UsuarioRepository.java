package com.example.farmatec.repository;

import com.example.farmatec.model.Sesion;
import com.example.farmatec.model.Usuario;
import java.sql.*;
import java.time.LocalDateTime;

public class UsuarioRepository {

    public Sesion autenticarYAbrirSesion(String nombreUsuario, String credencial) {
        String sqlUser = "SELECT * FROM USUARIO WHERE nombre = ? AND credencial = ?";
        String sqlSesion = "INSERT INTO SESION (id_usuario, inicio) VALUES (?, NOW())";

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement psUser = conn.prepareStatement(sqlUser)) {
            psUser.setString(1, nombreUsuario);
            psUser.setString(2, credencial);

            try (ResultSet rs = psUser.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("rol"),
                            rs.getString("credencial")
                    );

                    try (PreparedStatement psSes = conn.prepareStatement(sqlSesion, Statement.RETURN_GENERATED_KEYS)) {
                        psSes.setInt(1, u.getIdUsuario());
                        psSes.executeUpdate();
                        try (ResultSet rsSes = psSes.getGeneratedKeys()) {
                            if (rsSes.next()) {
                                Sesion sesion = new Sesion(rsSes.getInt(1), u.getIdUsuario(), LocalDateTime.now(), null);
                                sesion.setUsuario(u);
                                return sesion;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cerrarSesionBD(Sesion sesion) {
        if (sesion == null) return;
        sesion.cerrarSesion();
        String sql = "UPDATE SESION SET fin = NOW() WHERE id_sesion = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, sesion.getIdSesion());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}