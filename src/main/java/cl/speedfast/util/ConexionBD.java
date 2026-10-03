package cl.speedfast.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Reemplazar la URL con la ruta de la BBDD.
    private static final String URL =
            "jdbc:mysql://127.0.0.1:3306/speedfast_db";

    // Reemplazar "usuario" y "contraseña" por las credenciales del servicio local de MySQL.
    private static final String USER = "usuario";
    private static final String PASSWORD = "contrasenia";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
