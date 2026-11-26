package com.example.farmatec.repository;

import com.example.farmatec.model.Medicamento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoRepository {

    public boolean guardar(Medicamento med) {
        String sql = "INSERT INTO MEDICAMENTO (codigo_barras, marca, nombre, concentracion, presentacion, precio_compra, precio_venta, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, med.getCodigoBarras());
            ps.setString(2, med.getMarca());
            ps.setString(3, med.getNombre());
            ps.setString(4, med.getConcentracion());
            ps.setString(5, med.getPresentacion());
            ps.setDouble(6, med.getPrecioCompra());
            ps.setDouble(7, med.getPrecioVenta());
            ps.setString(8, med.getUbicacion());

            int filas = ps.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        med.setIdMedicamento(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizar(Medicamento med) {
        String sql = "UPDATE MEDICAMENTO SET marca = ?, nombre = ?, concentracion = ?, presentacion = ?, precio_compra = ?, precio_venta = ?, ubicacion = ? WHERE id_medicamento = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, med.getMarca());
            ps.setString(2, med.getNombre());
            ps.setString(3, med.getConcentracion());
            ps.setString(4, med.getPresentacion());
            ps.setDouble(5, med.getPrecioCompra());
            ps.setDouble(6, med.getPrecioVenta());
            ps.setString(7, med.getUbicacion());
            ps.setInt(8, med.getIdMedicamento());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Medicamento> buscarPorNombreOMarca(String criterio) {
        List<Medicamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM MEDICAMENTO WHERE nombre LIKE ? OR marca LIKE ? OR codigo_barras LIKE ? ORDER BY nombre ASC";
        String filtro = "%" + (criterio == null ? "" : criterio.trim()) + "%";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearMedicamento(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Medicamento obtenerPorCodigo(String codigo) {
        String sql = "SELECT * FROM MEDICAMENTO WHERE codigo_barras = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearMedicamento(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Medicamento mapearMedicamento(ResultSet rs) throws SQLException {
        return new Medicamento(
                rs.getInt("id_medicamento"),
                rs.getString("codigo_barras"),
                rs.getString("marca"),
                rs.getString("nombre"),
                rs.getString("concentracion"),
                rs.getString("presentacion"),
                rs.getDouble("precio_compra"),
                rs.getDouble("precio_venta"),
                rs.getString("ubicacion")
        );
    }
}