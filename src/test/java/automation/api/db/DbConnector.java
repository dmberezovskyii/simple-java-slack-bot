package automation.api.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnector {
    private static DbConnector instance;

    private static Connection connection;
    private static final String DATABASE_URL = "jdbc:mysql:";
    private static final String USERNAME = "uname";
    private static final String PASSWORD = "upassword";

    private DbConnector() {
    }

    public static DbConnector getInstance() {
        if (instance == null) {
            instance = new DbConnector();
        }
        return instance;
    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DATABASE_URL
                        , USERNAME
                        , PASSWORD);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null) try {
                    System.out.println("Connections is open");

                } catch (Exception ignored) {
                }
            }
        }
        return connection;
    }
}
