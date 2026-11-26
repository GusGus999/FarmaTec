package com.example.farmatec.repository;

import com.example.farmatec.model.DetalleVenta;
import com.example.farmatec.model.Venta;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class VentaRepository {

    public boolean guardar(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO VENTA (fecha_hora, id_sesion, total) VALUES (NOW(), ?, ?)";
        String sqlDetalle = "INSERT INTO DETALLE_VENTA (id_venta, id_lote, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock = """
                UPDATE LOTE
                SET cantidad = cantidad - ?,
                    estado = CASE WHEN (cantidad - ?) <= 0 THEN 'Agotado' ELSE estado END
                WHERE id_lote = ? AND cantidad >= ?
                """;
        String sqlLog = "INSERT INTO REGISTRO_TRANSACCION (tipo, fecha_hora, id_sesion, id_lote, cantidad) VALUES ('VENTA', NOW(), ?, ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionBD.obtenerConexion();
            conn.setAutoCommit(false);

            int idVentaGenerada;
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                psVenta.setInt(1, venta.getIdSesion());
                psVenta.setDouble(2, venta.getTotal());
                psVenta.executeUpdate();
                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (!rs.next()) {
                        conn.rollback();
                        return false;
                    }
                    idVentaGenerada = rs.getInt(1);
                    venta.setIdVenta(idVentaGenerada);
                }
            }

            try (PreparedStatement psDet = conn.prepareStatement(sqlDetalle);
                 PreparedStatement psStock = conn.prepareStatement(sqlStock);
                 PreparedStatement psLog = conn.prepareStatement(sqlLog)) {

                for (DetalleVenta det : detalles) {
                    psDet.setInt(1, idVentaGenerada);
                    psDet.setInt(2, det.getIdLote());
                    psDet.setInt(3, det.getCantidad());
                    psDet.setDouble(4, det.getPrecioUnitario());
                    psDet.executeUpdate();

                    psStock.setInt(1, det.getCantidad());
                    psStock.setInt(2, det.getCantidad());
                    psStock.setInt(3, det.getIdLote());
                    psStock.setInt(4, det.getCantidad());
                    if (psStock.executeUpdate() == 0) {
                        conn.rollback();
                        return false;
                    }

                    psLog.setInt(1, venta.getIdSesion());
                    psLog.setInt(2, det.getIdLote());
                    psLog.setInt(3, det.getCantidad());
                    psLog.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    public int obtenerTotalVendido(int idMedicamento, LocalDate inicio, LocalDate fin) {
        String sql = """
                SELECT COALESCE(SUM(dv.cantidad), 0) AS total_vendido
                FROM DETALLE_VENTA dv
                INNER JOIN VENTA v ON dv.id_venta = v.id_venta
                INNER JOIN LOTE l ON dv.id_lote = l.id_lote
                WHERE l.id_medicamento = ? AND DATE(v.fecha_hora) BETWEEN ? AND ?
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            ps.setDate(2, Date.valueOf(inicio));
            ps.setDate(3, Date.valueOf(fin));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_vendido");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}