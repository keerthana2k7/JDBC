package org.example;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/>rc="AllIcons.Actions.Execute"/> icon in the gutter.

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.SQLException;

public class Main {
        public static void main(String[] args) throws SQLException {
            PGSimpleDataSource ds = new PGSimpleDataSource();
            ds.setServerName("localhost");
            ds.setPortNumber(5434);
            ds.setDatabaseName("keerthana_db");
            ds.setUser("keerthana");
            ds.setPassword("password");

            MovieDao dao = new MovieDao(ds);

            try {
                dao.save(new Movie(1, "Interstellar", "Nolan"));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // UPDATE
            dao.update(new Movie(1, "Inception", "Christopher Nolan"));

            // FIND
            Movie movie = dao.findById(1);
            System.out.println(movie);

//        // DELETE
            dao.delete(1);
        }
}
