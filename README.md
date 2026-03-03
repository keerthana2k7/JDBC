# SQL Builder JDBC Demo

This project demonstrates a simple CRUD application using JDBC and a PostgreSQL database.

## Previous Implementation (SqlBuilder)

Initially, the project used the `SqlBuilder` library for database operations. Below is a reference of how it was implemented:

### MovieDao.java with SqlBuilder

```java
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

    public void update(Movie movie) throws SQLException {
        int rows = SqlBuilder
                .prepareSql("UPDATE movies SET name = ?, directed_by = ? WHERE id = ?")
                .param(movie.name())
                .param(movie.directedBy())
                .param(movie.id())
                .execute(dataSource);
        System.out.println("Rows updated: " + rows);
    }

    public void delete(int id) {
        try {
            int rows = SqlBuilder
                    .prepareSql("DELETE FROM movies WHERE id = ?")
                    .param(id)
                    .execute(dataSource);
            System.out.println("Rows deleted: " + rows);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
```

## Setup and Running

1. **Database**: Start the PostgreSQL database using Docker Compose:
   ```bash
   docker-compose up -d
   ```
2. **Schema**: The `init.sql` file will automatically create the `movies` table.
3. **Tests**: Run the test cases to verify CRUD operations:
   ```bash
   mvn test
   ```
