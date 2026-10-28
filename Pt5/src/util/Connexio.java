package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Connexio {
    private Connexio() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                configuracio("BOTIGA_DB_URL", "jdbc:mysql://localhost:3306/botiga"),
                configuracio("BOTIGA_DB_USER", "root"),
                configuracio("BOTIGA_DB_PASSWORD", ""));
    }

    private static String configuracio(String nom, String valorPerDefecte) {
        String valor = System.getenv(nom);
        return valor == null ? valorPerDefecte : valor;
    }
}
