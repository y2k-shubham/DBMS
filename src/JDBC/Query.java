/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JDBC;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * @author Y2K
 */
public class Query {

    private final java.sql.Statement statement;
    private static final Scanner SCANNER = new Scanner(System.in);

    public Query() {
        this.statement = Statement.getStatement();
    }

    private void select() throws SQLException {
        ResultSet resultSet;

        resultSet = statement.executeQuery("SELECT * FROM Account");
        System.out.println("\nA/C No\tBalance");

        while (resultSet.next()) {
            System.out.println(resultSet.getString("Account_No") + "\t" + resultSet.getString("Balance"));
        }
    }

    private void insert() throws SQLException {
        String accountNumber;
        float balance;

        System.out.println("\nEnter the data to be inserted:-");

        System.out.print("A/C No:\t");
        SCANNER.nextLine();
        accountNumber = SCANNER.nextLine();

        System.out.print("Bal:\t");
        balance = SCANNER.nextFloat();

        statement.executeUpdate("INSERT INTO Account (Account_Number, Balance) VALUES ('" + accountNumber + "', " + balance + ")");
    }

    private void update() throws SQLException {
        String accountNumber;
        float balance;

        System.out.println("\nEnter the data to be updated:-");

        System.out.print("A/C No:\t");
        SCANNER.nextLine();
        accountNumber = SCANNER.nextLine();

        System.out.print("Bal:\t");
        balance = SCANNER.nextFloat();

        statement.executeUpdate("UPDATE Account SET Balance = " + balance + " WHERE Account_No = '" + accountNumber + "'");
    }

    private void delete() throws SQLException {
        String accountNumber;

        System.out.println("\nEnter the A/C to be deleted:-");

        System.out.print("A/C No:\t");
        SCANNER.nextLine();
        accountNumber = SCANNER.nextLine();

        statement.executeUpdate("DELETE FROM Account_5 WHERE Account_Number = '" + accountNumber + "'");
    }

    public static void main(String[] args) {
        Query query;
        short choice;

        query = new Query();
        choice = -1;

        while (choice != 5) {
            System.out.println("\nChoose a task:-");
            System.out.println("1\tSELECT");
            System.out.println("2\tINSERT");
            System.out.println("3\tUPDATE");
            System.out.println("4\tDELETE");
            System.out.println("5\tEXIT");

            System.out.print("\nEnter your choice:\t");
            choice = SCANNER.nextShort();

            switch (choice) {
                case 1:
                    try {
                        query.select();
                    } catch (SQLException exception) {
                        System.out.println("Error:\t" + exception.getMessage());
                    } finally {
                        break;
                    }

                case 2:
                    try {
                        query.insert();
                    } catch (SQLException exception) {
                        System.out.println("Error:\t" + exception.getMessage());
                    } finally {
                        break;
                    }

                case 3:
                    try {
                        query.update();
                    } catch (SQLException exception) {
                        System.out.println("Error:\t" + exception.getMessage());
                    } finally {
                        break;
                    }

                case 4:
                    try {
                        query.delete();
                    } catch (SQLException exception) {
                        System.out.println("Error:\t" + exception.getMessage());
                    } finally {
                        break;
                    }

                case 5:
                    try {
                        Statement.closeConnections();
                    } catch (SQLException exception) {
                        System.out.println("Error:\t" + exception.getMessage());
                    } finally {
                        continue;
                    }

                default:
                    System.out.println("\nInvalid Input\n");
            }
        }
    }

}
