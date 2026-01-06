package org.example.integration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;

public class SimpleScriptRunner {


    public static void run(Connection connection, InputStream sqlFile) {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(sqlFile));
             Statement statement = connection.createStatement()) {

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                sql.append(line);

                if (line.endsWith(";")) {
                    statement.execute(sql.toString());
                    sql.setLength(0);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error executing SQL script", e);
        }
    }
}
