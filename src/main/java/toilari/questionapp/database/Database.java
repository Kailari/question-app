package toilari.questionapp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.RequiredArgsConstructor;

/**
 * A wrapper for database connection.
 */
@RequiredArgsConstructor
public class Database {
    private final String databaseAddress;

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.databaseAddress);
    }
}
