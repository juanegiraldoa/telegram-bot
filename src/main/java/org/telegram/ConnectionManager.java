package org.telegram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "";
    private static final String USER = "";
    private static final String PASSWORD = "";

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    public static void close(Connection connection, PreparedStatement preparedStatement) {
        close(connection);
        try {
            preparedStatement.close();
        } catch (SQLException ignored) {
        }
    }

    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        close(connection, preparedStatement);
        try {
            resultSet.close();
        } catch (SQLException ignored) {
        }
    }
}
