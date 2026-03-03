package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class MovieDaoTest {

    private MovieDao movieDao;
    private PGSimpleDataSource dataSource;

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = new PGSimpleDataSource();
        dataSource.setServerNames(new String[]{"localhost"});
        dataSource.setPortNumbers(new int[]{5434});
        dataSource.setDatabaseName("keerthana_db");
        dataSource.setUser("keerthana");
        dataSource.setPassword("password");

        movieDao = new MovieDao(dataSource);

        // Clear the table before each test
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE movies RESTART IDENTITY");
        }
    }

    @Test
    void save() throws SQLException {
        Movie movie = new Movie(null, "Inception", "Christopher Nolan");
        movieDao.save(movie);

        Movie savedMovie = movieDao.findById(1);
        assertNotNull(savedMovie);
        assertEquals("Inception", savedMovie.name());
        assertEquals("Christopher Nolan", savedMovie.directedBy());
    }

    @Test
    void update() throws SQLException {
        Movie movie = new Movie(null, "Interstellar", "Nolan");
        movieDao.save(movie);

        Movie updateMovie = new Movie(1, "Interstellar", "Christopher Nolan");
        movieDao.update(updateMovie);

        Movie result = movieDao.findById(1);
        assertEquals("Christopher Nolan", result.directedBy());
    }

    @Test
    void delete() throws SQLException {
        Movie movie = new Movie(null, "The Dark Knight", "Nolan");
        movieDao.save(movie);

        movieDao.delete(1);
        Movie result = movieDao.findById(1);
        assertNull(result);
    }

    @Test
    void findById() throws SQLException {
        Movie movie = new Movie(null, "Dunkirk", "Nolan");
        movieDao.save(movie);

        Movie result = movieDao.findById(1);
        assertNotNull(result);
        assertEquals("Dunkirk", result.name());
    }
}