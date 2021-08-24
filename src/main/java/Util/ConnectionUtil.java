package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
    private static Connection connection;

    public static Connection getConnection() {

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(System.getenv("db_host"), System.getenv("db_root"), System.getenv("db_pass"));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
