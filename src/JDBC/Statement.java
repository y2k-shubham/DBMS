/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Y2K
 */
public class Statement {

    private static Connection connection;
    private static java.sql.Statement statement;

    private static Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "iiita");
        return connection;
    }

    public static java.sql.Statement getStatement() {
        try {
            statement = getConnection().createStatement();
        } catch (SQLException exception) {
            statement = null;
        }
        return statement;
    }

    public static void closeConnections() throws SQLException {
        statement.close();
        connection.close();
    }
}
