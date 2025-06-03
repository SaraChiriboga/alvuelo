import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseAlvuelo {
    String url = "jdbc:mysql://localhost:3306/alvuelo_db?user=root";
    String user = "root";
    String password = "p3alvuelo";

    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos.");
            return conn;
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
            return null;
        }
    }
}
