package org.example.integration;

import java.sql.Connection;
import java.sql.DriverManager;

public class H2TestDatabase {

    public static Connection createConnection() throws Exception {
        return DriverManager.getConnection(
                "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"
        );
    }
}
