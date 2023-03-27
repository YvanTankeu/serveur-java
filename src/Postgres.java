import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ON utilisera la librairie postgresql-42.6.0 comme driver de base de donnee
public class Postgres { 
    final String driverClassName = "org.postgresql.Driver";
    final String url = "jdbc:postgresql://localhost:5432/meteo";
    final String username = "postgres";
    final String password = "admin";

    /**
     * Connectez-vous à la base de données PostgreSQL
     *
     * @return un objet Connexion
     */
    public Connection connect() throws SQLException{
        return DriverManager.getConnection(url, username, password);
    }
}
