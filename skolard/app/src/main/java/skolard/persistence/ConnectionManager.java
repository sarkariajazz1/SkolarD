package skolard.persistence;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    private static Connection connection;

    public static void initialize(String dbPath) throws Exception {
        if (connection != null) return;

        Class.forName("org.sqlite.JDBC"); // ✅ This is where JDBC is loaded
        connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

    public static Connection get() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null) connection.close();
        } catch (Exception ignored) {}
    }
}
