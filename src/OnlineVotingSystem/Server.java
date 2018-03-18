/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineVotingSystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Y2K
 */
public class Server implements Runnable {
    
    Socket socket;
    PollingAgentGUI pollingAgentGUI;
    private Scanner scanner;
    private PrintWriter printWriter;
    String message;
    int index;
    
    public Server(Socket socket, PollingAgentGUI pollingAgentGUI, int index) throws IOException {
        this.socket = socket;
        this.pollingAgentGUI = pollingAgentGUI;
        this.index = index;
        
        scanner = new Scanner(socket.getInputStream());
        printWriter = new PrintWriter(socket.getOutputStream());
        
    }
    
    public void checkConnection() throws IOException {
        int i;
        PrintWriter printWriter;
        Socket socket;
        
        if (!this.socket.isConnected()) {
            // like we've removed this socket from list of sockets (below), we must also remove this server from list of servers if
            // socket isn't connected
            for (i = 1; i <= PollingAgent.socket.size(); i++) {
                if (PollingAgent.socket.get(i) == this.socket) {
                    PollingAgent.socket.remove(i);
                }
            }
            
            for (i = 1; i <= PollingAgent.socket.size(); i++) {
                socket = (Socket) PollingAgent.socket.get(i - 1);
                //printWriter = new PrintWriter(socket.getOutputStream());
                // what's this stupidity? why would you scane through the list of sockets and print in colsole that it has
                // disconnected. Only this.socket is disconnected, not every socket in the arraylist
                System.out.println(socket.getLocalAddress().getHostName() + " disconnected");
            }
        }
    }
    
    @Override
    public void run() {
        try {
            try {
                while (true) {
                    checkConnection();
                    
                    if (!scanner.hasNext()) {
                        return;
                    } else {
                        message = scanner.nextLine();
                        if (message.equalsIgnoreCase("Done")) {
                            pollingAgentGUI.changeClientStatus(index, true);
                            System.out.println("Recieved Done from Client");
                        }
                    }
                }
            } finally {
                System.out.println("Closing Socket for client:\t" + index);
                socket.close();
            }
        } catch (IOException exception) {
            System.out.println("Error:\t" + exception.getMessage());
        }
    }
    
    public void activateClient() {
        String clientID;
        
        clientID = pollingAgentGUI.textFieldClient[index].getText();
        System.out.println("Sending message:\t" + clientID + "\tto client:\t" + index);
        printWriter.println(clientID);
        printWriter.flush();
        pollingAgentGUI.changeClientStatus(index, false);
    }
    
}
