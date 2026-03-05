package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;


public class QueryExample {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = getDataSource();

        Long generatedId = null;

        // S1

        String sql = "INSERT INTO movie(title, directed_by) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "Interstellar");
            ps.setString(2, "Nolan");

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getLong(1);
                }
            }
        }

        System.out.println("Movie Created with id # "  + generatedId);

        // S 2

        sql = "{? = call insert_movie_fn(?, ?)}";

        try (Connection connection = dataSource.getConnection();
             CallableStatement cs = connection.prepareCall(sql)) {

            // Register OUT parameter (return value)
            cs.registerOutParameter(1, Types.BIGINT);

            // Set IN parameters
            cs.setString(2, "Inception");
            cs.setString(3, "Christopher Nolan");

            // Execute
            cs.execute();

            // Retrieve OUT parameter
            generatedId = cs.getLong(1);
        }

        System.out.println("Movie Created ( through Stored Procedure ) with id # "  + generatedId);

        // S3

        int[] updatedRows;

        sql = "INSERT INTO movie(title, directed_by) VALUES (?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            // First batch entry
            ps.setString(1, "Interstellar");
            ps.setString(2, "Nolan");
            ps.addBatch();

            // Second batch entry
            ps.setString(1, "Dunkrik");
            ps.setString(2, "Nolan");
            ps.addBatch();

            // Execute batch
            updatedRows = ps.executeBatch();
        }

        System.out.println(Arrays.stream(updatedRows).sum() + " movies were created as Batch ");

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
