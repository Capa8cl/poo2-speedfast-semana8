package cl.speedfast.dao;

import cl.speedfast.model.Entrega;
import cl.speedfast.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Clase DAO que gestiona persistencia de la entrega del Pedido.
 */
public class EntregaDAO {

    /**
     *
     * Registra una nueva Entrega en la base de datos.
     *
     * @param entrega Objeto de la Entrega.
     * @return Retorna true si la entrega fue guardada correctamente.
     */
    public boolean guardar(Entrega entrega) {
        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, entrega.getFecha());
            ps.setTime(4, entrega.getHora());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al guardar la entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Lista todas las entregas de la base de datos.
     *
     * @return Lista de Entregas.
     */
    public List<Entrega> listarTodos() {
        List<Entrega> lista = new ArrayList<>();
        String sql = "SELECT id, id_pedido, id_repartidor, fecha, hora FROM entregas";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int idPedido = rs.getInt("id_pedido");
                int idRepartidor = rs.getInt("id_repartidor");
                Date fecha = rs.getDate("fecha");
                Time hora = rs.getTime("hora");

                Entrega entrega = new Entrega(id, idPedido, idRepartidor, fecha, hora);
                lista.add(entrega);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar entregas: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Elimina una Entrega por su ID.
     *
     * @param id ID de la Entrega.
     * @return Retorna true si fue eliminada correctamente.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM entregas WHERE id = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrega: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Edita una entrega.
     *
     * @param entrega Objeto entrega.
     * @return Retorna true si fue editada correctamente.
     */
    public boolean actualizar(Entrega entrega) {
        String sql = "UPDATE entregas SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, entrega.getFecha());
            ps.setTime(4, entrega.getHora());
            ps.setInt(5, entrega.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: " + e.getMessage());
            return false;
        }
    }
}
