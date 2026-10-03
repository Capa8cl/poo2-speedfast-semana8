package cl.speedfast.dao;

import cl.speedfast.model.Repartidor;
import cl.speedfast.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Clase DAO que gestiona persistencia de los datos del Repartidor del Pedido.
 */
public class RepartidorDAO {

    /**
     *
     * Registra un nuevo Repartidor en la base de datos.
     *
     * @param repartidor Objeto Entrega.
     * @return Retorna true si el repartidor fue guardado correctamente.
     */
    public boolean guardar(Repartidor repartidor) {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, repartidor.getNombreRepartidor());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        repartidor.setIdRepartidor(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al guardar el Repartidor: " + e.getMessage());
        }
        return false;
    }

    /**
     *
     * Lista todos los repartidores.
     *
     * @return Lista Repartidores.
     */
    public List<Repartidor> listarTodos() {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT id, nombre FROM repartidores";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                lista.add(new Repartidor(id, nombre, null));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar repartidores: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Edita un Repartidor en la base de datos.
     *
     * @param repartidor Nombre repartidor
     * @return Retorna true si el repartidor fue editado correctamente.
     */
    public boolean actualizar(Repartidor repartidor) {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, repartidor.getNombreRepartidor());
            ps.setInt(2, repartidor.getIdRepartidor());

            return ps.executeUpdate() > 0;

            } catch (SQLException ex) {
            System.out.println("Error al actualizar repartidor: " + ex.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un Repartidor en la base de datos.
     *
     * @param id ID del Repartidor.
     * @return Retorna true si el repartidor fue eliminado correctamente.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM repartidores WHERE id = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar repartidor: " + e.getMessage());
            return false;
        }
    }
}
