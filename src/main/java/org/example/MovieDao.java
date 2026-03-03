package org.example;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDao {
    private PGSimpleDataSource dataSource;

    public MovieDao(PGSimpleDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Movie movie) throws SQLException {
        int rows = SqlBuilder
                .prepareSql("INSERT INTO movies (name, directed_by) VALUES (?, ?)")
                .param(movie.name())
                .param(movie.directedBy())
                .execute(dataSource);
        System.out.println("Rows inserted: " + rows);
    }

    // update
    public void update(Movie movie) throws SQLException {
        String sql = "UPDATE movies SET name = ?, directed_by = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, movie.name());
            pstmt.setString(2, movie.directedBy());
            pstmt.setInt(3, movie.id());
            int rows = pstmt.executeUpdate();
            System.out.println("Rows updated: " + rows);
        }
    }

    // delete
    public void delete(int id) {
        String sql = "DELETE FROM movies WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            System.out.println("Rows deleted: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // findById
    public Movie findById(int id) {
        String sql = "SELECT id, name, directed_by FROM movies WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Movie(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("directed_by"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
