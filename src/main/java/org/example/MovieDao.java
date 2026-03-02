package org.example;

import org.postgresql.ds.PGSimpleDataSource;
import org.tamilnadujug.SqlBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDao {
  private  PGSimpleDataSource dataSource;
  public MovieDao(PGSimpleDataSource dataSource) {
      this.dataSource=dataSource;
  }

    public void save(Movie movie) throws SQLException {

        int rows = SqlBuilder
                .prepareSql("INSERT INTO movies (name, directed_by) VALUES (?, ?)")
                .param(movie.name())
                .param(movie.directedBy())
                .execute(dataSource);

        System.out.println("Rows inserted: " + rows);
    }


    //update
    public void update(Movie movie) throws SQLException {

        int rows = SqlBuilder
                .prepareSql("UPDATE movies SET name = ?, directed_by = ? WHERE id = ?")
                .param(movie.name())
                .param(movie.directedBy())
                .param(movie.id())
                .execute(dataSource);

        System.out.println("Rows updated: " + rows);
    }



    //delete
    public void delete(int id) {

        int rows = 0;
        try {
            rows = SqlBuilder
                    .prepareSql("DELETE FROM movies WHERE id = ?")
                    .param(id)
                    .execute(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Rows deleted: " + rows);
    }



    //findById
    public Movie findById(int id) {

        try {
            return SqlBuilder
                    .prepareSql("SELECT id, name, directed_by FROM movies WHERE id = ?")
                    .param(id)
                    .queryForOne(rs -> new Movie(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("directed_by")
                    ))
                    .execute(dataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
