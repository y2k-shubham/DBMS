/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.mail.search.RecipientStringTerm;

/**
 *
 * @author Y2K
 */
public class Client_1 {

    public static void main(String[] args) {
        Client_1 client_1;
        client_1 = new Client_1();

        try {
            client_1.run();
        } catch (IOException iOException) {
            System.out.println("Error:\t" + iOException.getMessage());
        }
    }

    private void run() throws IOException {
        Socket socket;
        BufferedReader bufferedReader;
        PrintStream printStream;
        String receievedMessage;
        String sentMessage;
        Scanner scanner;

        scanner = new Scanner(System.in);
        socket = new Socket("localhost", 444);
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printStream = new PrintStream(socket.getOutputStream());

        System.out.print("Enter message to be sent to Server:\t");
        sentMessage = scanner.nextLine();

        printStream.println(sentMessage);
        receievedMessage = bufferedReader.readLine();

        if (receievedMessage != null) {
            System.out.println("Recieved Message:\t\t\t" + receievedMessage);
        } else {
            System.out.println("No message recieved from server");
        }

        scanner.close();
        printStream.close();
        bufferedReader.close();
        socket.close();
    }
}
