/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Y2K
 */
public class Driver {

    public static void main(String[] args) {
        Connection connection;
        Statement statement;
        ResultSet resultSet;

        connection = null;
        statement = null;
        resultSet = null;

        try {
            // Get connection to Database
            connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "system", "iiita");

            // Create statement
            statement = connection.createStatement();

            // Execute SQL Query
            resultSet = statement.executeQuery("SELECT * FROM Account");

            // Process the ResultSet
            System.out.println("A/C No\t\tBalance\n");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("Account_no") + "\t" + resultSet.getString("Balance"));
            }
        } catch (Exception exception) {
            System.out.println("Exception:\t" + exception.getMessage());
        } finally {
            try {
                connection.commit();
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException exception) {
                System.out.println("Error:\t" + exception.getMessage());
            }
        }
    }
}
