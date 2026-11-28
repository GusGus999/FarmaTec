package com.example.farmatec.repository;

import com.example.farmatec.model.Descuento;
import com.example.farmatec.model.Lote;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class LoteRepository {

    public boolean guardar(Lote lote) {
        String sql = "INSERT INTO LOTE (id_medicamento, numero_lote, fecha_caducidad, cantidad, fecha_ingreso, estado) VALUES (?, ?, ?, ?, NOW(), ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, lote.getIdMedicamento());
            ps.setString(2, lote.getNumeroLote());
            ps.setDate(3, Date.valueOf(lote.getFechaCaducidad()));
            ps.setInt(4, lote.getCantidad());
            ps.setString(5, lote.getEstado() != null ? lote.getEstado() : "Vigente");

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        lote.setIdLote(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Lote> obtenerDisponiblesPorMedicamento(int idMedicamento) {
        List<Lote> lotes = new ArrayList<>();
        String sql = """
                SELECT l.*, m.nombre AS nombre_med, m.ubicacion, m.precio_venta
                FROM LOTE l
                INNER JOIN MEDICAMENTO m ON l.id_medicamento = m.id_medicamento
                WHERE l.id_medicamento = ? AND l.cantidad > 0
                ORDER BY l.fecha_caducidad ASC
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idMedicamento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lotes.add(mapearLote(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lotes;
    }

    public List<Lote> obtenerLotesConStock() {
        List<Lote> lotes = new ArrayList<>();
        String sql = """
                SELECT l.*, m.nombre AS nombre_med, m.ubicacion, m.precio_venta
                FROM LOTE l
                INNER JOIN MEDICAMENTO m ON l.id_medicamento = m.id_medicamento
                WHERE l.cantidad > 0
                ORDER BY l.fecha_caducidad ASC
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lotes.add(mapearLote(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lotes;
    }

    public boolean descontarStock(int idLote, int cantidad) {
        String sql = """
                UPDATE LOTE
                SET cantidad = cantidad - ?,
                    estado = CASE WHEN (cantidad - ?) <= 0 THEN 'Agotado' ELSE estado END
                WHERE id_lote = ? AND cantidad >= ?
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, cantidad);
            ps.setInt(3, idLote);
            ps.setInt(4, cantidad);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarEstado(int idLote, String nuevoEstado, boolean vaciarStock) {
        String sql = vaciarStock
                ? "UPDATE LOTE SET estado = ?, cantidad = 0 WHERE id_lote = ?"
                : "UPDATE LOTE SET estado = ? WHERE id_lote = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idLote);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean guardarDescuento(Descuento desc) {
        String sql = "INSERT INTO DESCUENTO (id_lote, porcentaje, fecha_inicio, fecha_fin, tipo_descuento, id_sesion) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, desc.getIdLote());
            ps.setDouble(2, desc.getPorcentaje());
            ps.setTimestamp(3, Timestamp.valueOf(desc.getFechaInicio()));
            ps.setTimestamp(4, Timestamp.valueOf(desc.getFechaFin()));
            ps.setString(5, desc.getTipoDescuento());
            ps.setInt(6, desc.getIdSesion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Descuento> obtenerDescuentosActivos() {
        List<Descuento> activos = new ArrayList<>();
        String sql = """
                SELECT d.*, m.nombre AS nombre_med, l.numero_lote
                FROM DESCUENTO d
                INNER JOIN LOTE l ON d.id_lote = l.id_lote
                INNER JOIN MEDICAMENTO m ON l.id_medicamento = m.id_medicamento
                WHERE NOW() BETWEEN d.fecha_inicio AND d.fecha_fin AND l.cantidad > 0
                """;
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Descuento d = new Descuento(
                        rs.getInt("id_descuento"),
                        rs.getInt("id_lote"),
                        rs.getDouble("porcentaje"),
                        rs.getTimestamp("fecha_inicio").toLocalDateTime(),
                        rs.getTimestamp("fecha_fin").toLocalDateTime(),
                        rs.getString("tipo_descuento"),
                        rs.getInt("id_sesion")
                );
                d.setDescripcionLote(rs.getString("nombre_med") + " (Lote " + rs.getString("numero_lote") + ") -" + d.getPorcentaje() + "%");
                activos.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return activos;
    }

    private Lote mapearLote(ResultSet rs) throws SQLException {
        Timestamp tsIngreso = rs.getTimestamp("fecha_ingreso");
        Lote lote = new Lote(
                rs.getInt("id_lote"),
                rs.getInt("id_medicamento"),
                rs.getString("numero_lote"),
                rs.getDate("fecha_caducidad").toLocalDate(),
                rs.getInt("cantidad"),
                tsIngreso != null ? tsIngreso.toLocalDateTime() : null,
                rs.getString("estado")
        );
        lote.setNombreMedicamento(rs.getString("nombre_med"));
        lote.setUbicacionMedicamento(rs.getString("ubicacion"));
        lote.setPrecioVentaBase(rs.getDouble("precio_venta"));
        return lote;
    }
}