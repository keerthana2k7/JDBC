package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TransactionExample {
    public static void main(String[] args) {

        DataSource dataSource = getDataSource();

        Connection connection = null;

        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false); // Start transaction

            long directorId = -1;

            // Step 1: Insert director and get generated ID
            String insertDirectorSql = "INSERT INTO director(name) VALUES (?)";
            try (PreparedStatement ps = connection.prepareStatement(
                    insertDirectorSql,
                    Statement.RETURN_GENERATED_KEYS)) {

                ps.setString(1, "Christopher Nolan");
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        directorId = rs.getLong(1);
                    }
                }
            }

            // Step 2: Fetch director name using ID
            String directorName = null;
            String selectSql = "SELECT name FROM director WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(selectSql)) {

                ps.setLong(1, directorId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        directorName = rs.getString(1);
                    }
                }
            }

            // Step 3: Insert movies using directorName
            String insertMovieSql =
                    "INSERT INTO movie(title, directed_by) VALUES (?, ?), (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertMovieSql)) {

                ps.setString(1, "Tenet");
                ps.setString(2, directorName);
                ps.setString(3, "Oppenheimer");
                ps.setString(4, directorName);

                ps.executeUpdate();
            }

            connection.commit(); // Commit transaction

        } catch (Exception e) {

            if (connection != null) {
                try {
                    connection.rollback(); // Rollback on error
                } catch (SQLException ignored) {}
            }

            throw new RuntimeException(e);

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {}
            }
        }
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
