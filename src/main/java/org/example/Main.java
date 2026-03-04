package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = getDataSource();

        String dbVersion = null;

        String sqlQuery = "select version();";

        // Formality - 1
        try(
                Connection connection = dataSource.getConnection(); // Formality - 2
                Statement statement = connection.createStatement(); // Formality - 3
                ResultSet resultSet = statement.executeQuery(sqlQuery) // Formality - 4
        ) {
            // Formality - 5
            if(resultSet.next()) {
                dbVersion = resultSet.getString(1);
            }
        }

        System.out.println(dbVersion);
    }


















    private static PGSimpleDataSource getDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setPortNumbers(new int[]{5432});
        dataSource.setDatabaseName("sampledb");
        dataSource.setUser("sampledb");
        dataSource.setPassword("sampledb");
        return dataSource;
    }
}
