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

/**
 *
 * @author Y2K
 */
public class Server_1 {

    public static void main(String[] args) {
        Server_1 server_1;
        server_1 = new Server_1();

        try {
            server_1.run();
        } catch (IOException iOException) {
            System.out.println("Error! : " + iOException.getMessage());
        }
    }

    private void run() throws IOException {
        ServerSocket serverSocket;
        Socket socket;
        BufferedReader bufferedReader;
        PrintStream printStream;
        String sentMessage;
        String recievedMessage;
        Scanner scanner;

        scanner = new Scanner(System.in);
        serverSocket = new ServerSocket(444);
        socket = serverSocket.accept();
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printStream = new PrintStream(socket.getOutputStream());

        System.out.print("Enter message to be sent to client:\t");
        sentMessage = scanner.nextLine();

        printStream.println(sentMessage);
        recievedMessage = bufferedReader.readLine();

        if (recievedMessage != null) {
            System.out.println("Recieved Message:\t\t\t" + recievedMessage);
        } else {
            System.out.println("No message recieved from client");
        }

        scanner.close();
        printStream.close();
        bufferedReader.close();
        socket.close();
        serverSocket.close();
    }
}
