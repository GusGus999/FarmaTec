package com.example.farmatec.repository;

import com.example.farmatec.model.RegistroTransaccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LogRepository {

    public boolean registrarLog(RegistroTransaccion log) {
        String sql = "INSERT INTO REGISTRO_TRANSACCION (tipo, fecha_hora, id_sesion, id_lote, cantidad) VALUES (?, NOW(), ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, log.getTipo());
            ps.setInt(2, log.getIdSesion());
            ps.setInt(3, log.getIdLote());
            ps.setInt(4, log.getCantidad());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}