package de.medieninformatik.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <i>
 *     Mit Zustimmung übernommene Klasse aus dem letzten Testat für Softwaretechnik 1, welches gemeinsam erarbeitet wurde.
 * </i>
 * <br><br>
 * Singleton class storing a connection to a database;
 * the connection will be tried to open using the root user and an empty password
 *
 * @author Malte Kasolowsky
 */
public class DBModel {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/informatik";

    private static final DBModel INSTANCE;
    private static final SQLException INSTANTIATION_EXCEPTION;

    static {

        //Create database here if not existing already

        DBModel inst = null;
        SQLException instantiationException = null;
        try {
            inst = new DBModel();
        }
        catch (SQLException e) {
            instantiationException = e;
        }
        INSTANCE = inst;
        INSTANTIATION_EXCEPTION = instantiationException;
    }

    private final Connection connection;

    /**
     * Private constructor, as only one static instantiation for the Singleton will be performed
     *
     * @throws SQLException If {@link DriverManager#getConnection(String, String, String)} fails
     */
    private DBModel() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, "minf", "prog3");
    }

    /**
     * Singleton getter
     *
     * @return The DBModel Singleton
     */
    static DBModel getInstance() {
        if (INSTANTIATION_EXCEPTION != null) {
            throw new IllegalStateException(
                    "a failure occurred previously when creating the DBModel instance",
                    INSTANTIATION_EXCEPTION
            );
        }
        return INSTANCE;
    }

    /**
     * Getter for the {@link Connection} to the database
     *
     * @return The connection
     */
    Connection getConnection() {
        return connection;
    }
}
