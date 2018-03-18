/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineVotingSystem;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Y2K
 */
public class Client implements Runnable {

    Scanner scanner;
    PrintWriter printWriter;
    Socket socket;

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        printWriter = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());
    }

    @Override
    public void run() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void votingDone() {
        printWriter.println("Done");
        printWriter.flush();
    }

}
