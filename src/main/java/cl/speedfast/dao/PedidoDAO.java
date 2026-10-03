package cl.speedfast.dao;

import cl.speedfast.model.EstadoPedido;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.TipoPedido;
import cl.speedfast.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;


/**
 *
 * Clase DAO que gestiona persistencia del Pedido.
 */
public class PedidoDAO {

    /**
     *
     * Registra un nuevo Pedido en la base de datos.
     *
     * @param pedido Objeto Pedido.
     * @return Retorna true si el pedido fue guardada correctamente.
     */
    public boolean guardar(Pedido pedido) {
        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipo().name());
            ps.setString(3, pedido.getEstado().name());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        pedido.setIdPedido(idGenerado);
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar el pedido: " + e.getMessage());
        }
        return false;
    }

    /**
     *
     * Lista todos los pedidos.
     *
     * @return Lista de Pedidos.
     */
    public List<Pedido> listarTodos() {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                TipoPedido tipo = TipoPedido.valueOf(rs.getString("tipo"));
                EstadoPedido estado = EstadoPedido.valueOf(rs.getString("estado"));

                lista.add(new Pedido(id, direccion, tipo, estado));
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar pedidos en la BD: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Lista los pedidos filtrados por tipo (COMIDA, ENCOMIENDA, EXPRESS).
     */
    public List<Pedido> listarPorTipo(TipoPedido tipo) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos WHERE tipo = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, tipo.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String direccion = rs.getString("direccion");
                    TipoPedido t = TipoPedido.valueOf(rs.getString("tipo"));
                    EstadoPedido e = EstadoPedido.valueOf(rs.getString("estado"));

                    lista.add(new Pedido(id, direccion, t, e));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por tipo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Lista los pedidos filtrados por estado (PENDIENTE, EN_REPARTO, ENTREGADO).
     */
    public List<Pedido> listarPorEstado(EstadoPedido estado) {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id, direccion, tipo, estado FROM pedidos WHERE estado = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estado.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String direccion = rs.getString("direccion");
                    TipoPedido t = TipoPedido.valueOf(rs.getString("tipo"));
                    EstadoPedido e = EstadoPedido.valueOf(rs.getString("estado"));

                    lista.add(new Pedido(id, direccion, t, e));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar pedidos por estado: " + e.getMessage());
        }
        return lista;
    }

    /**
     *
     * Actualiza el Estado de un Pedido en la base de datos.
     *
     * @param idPedido    ID del Pedido.
     * @param nuevoEstado Nuevo esta
     * @return Devuelve true si se actualizó de manera correcta y false si da error al actualizar.
     */
    public boolean actualizarEstado(int idPedido, EstadoPedido nuevoEstado) {
        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idPedido);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del pedido #" + idPedido + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza un Pedido completo en la base de datos (Dirección, Tipo y Estado).
     *
     * @param pedido Objeto Pedido con los datos actualizados.
     * @return Retorna true si fue actualizado correctamente.
     */
    public boolean actualizar(Pedido pedido) {
        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccionEntrega());
            ps.setString(2, pedido.getTipo().name());
            ps.setString(3, pedido.getEstado().name());
            ps.setInt(4, pedido.getIdPedido());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pedido: " + e.getMessage());
            return false;
        }
    }

    /**
     *
     * Elimina un Pedido en la base de datos.
     *
     * @param id ID del Pedido.
     * @return Retorna true si el pedido fue eliminado correctamente.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM pedidos WHERE id = ?";
        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error al eliminar pedido: " + e.getMessage());
            return false;
        }
    }
}