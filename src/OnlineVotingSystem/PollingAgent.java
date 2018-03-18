/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineVotingSystem;

import JDBC.Statement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

/**
 *
 * @author Y2K
 */
public class PollingAgent implements ActionListener {

    ServerSocket serverSocket;
    static ArrayList<Socket> socket;
    static ArrayList<Server> servers;
    int port;
    int maxNumberOfClients;
    int currNumberOfClients;
    PollingAgentGUI pollingAgentGUI;

    public PollingAgent() throws IOException {
        port = 444;
        maxNumberOfClients = 3;
        currNumberOfClients = 0;
        serverSocket = new ServerSocket(port);
        socket = new ArrayList<>(maxNumberOfClients);
        servers = new ArrayList<>(maxNumberOfClients);
        pollingAgentGUI = new PollingAgentGUI();
    }

    private void startClients() throws IOException {
        int i;
        Socket socket;
        Server server;
        Thread thread;

        System.out.println("Waiting for Clients..");
        for (i = 0; i < maxNumberOfClients; i++) {
            socket = serverSocket.accept();
            
            this.socket.add(socket);
            server = new Server(socket, pollingAgentGUI, i);
            this.servers.add(server);
            
            thread = new Thread(server);
            thread.start();

            pollingAgentGUI.buttonClient[i].setEnabled(true);
            pollingAgentGUI.textFieldClient[i].setEnabled(true);
            currNumberOfClients++;

            System.out.println("Connected to Client:\t" + socket.getLocalAddress().getHostName());
        }
    }

    public static void main(String[] args) {
        PollingAgent pollingAgent;

        try {
            pollingAgent = new PollingAgent();
        } catch (IOException iOException) {
            pollingAgent = null;
            System.out.println("Error in calling constructor:\t" + iOException.getMessage());
        } catch (Exception e) {
            pollingAgent = null;
            System.out.println("Error : " + e.getMessage());
        }

        pollingAgent.pollingAgentGUI.createFrame();
        pollingAgent.addListeners();

        //while (pollingAgent.pollingAgentGUI.buttonLogin.isEnabled()) {
        //}
        try {
            pollingAgent.startClients();
        } catch (IOException iOException) {
            System.out.println("Error IOException in starting clients:\t" + iOException);
        } catch (NullPointerException nullPointerException) {
            System.out.println("Error NullPointerException in starting clients:\t" + nullPointerException);
        } catch (Exception exception) {
            System.out.println("Error Exception in starting clients:\t" + exception);
        }
    }

    private void addListeners() {
        int i;

        pollingAgentGUI.buttonLogin.addActionListener(this);

        for (i = 0; i < pollingAgentGUI.maxNumberOfClients; i++) {
            pollingAgentGUI.textFieldClient[i].addActionListener(this);
            pollingAgentGUI.buttonClient[i].addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int i;
        int j;
        //String component;
        //component = e.getActionCommand();

        if (e.getSource() == pollingAgentGUI.buttonLogin) {
            if (pollingAgentGUI.buttonLogin.getText().equalsIgnoreCase("Logout")) {
                pollingAgentGUI.buttonLogin.setText("Login");
                pollingAgentGUI.changeButtonState(false);
                pollingAgentGUI.clearLogin();
                pollingAgentGUI.clearClients();
                pollingAgentGUI.changeLoginState(true);
                pollingAgentGUI.changeClientState(false);
            } else if (!pollingAgentGUI.textFieldAgentID.getText().isEmpty() && !pollingAgentGUI.textFieldPassword.getText().isEmpty()) {
                //pollingAgentGUI.changeButtonState(true);
                pollingAgentGUI.changeLoginState(false);
                //pollingAgentGUI.changeClientState(true);
                pollingAgentGUI.buttonLogin.setText("Logout");
            }
        }

        for (i = 0; i < currNumberOfClients; i++) {
            if (e.getSource() == pollingAgentGUI.buttonClient[i]) {
                //System.out.println("\nActivate Action recieved, looking for match..");
                try {
                    if (isPresent(pollingAgentGUI.textFieldClient[i].getText())) {
                        System.out.println("\n\nThis person has already voted");
                        pollingAgentGUI.textFieldClient[i].setText("");
                        return;
                    }
                } catch (Exception exc) {
                    System.out.println("\nError in determining if person has already voted: " + exc.getMessage());
                }

                for (j = 0; j <= servers.size() - 1; j++) {
                    if (servers.get(j).index == i) {
                        //System.out.println("Activate Action matched, activating client..");
                        servers.get(j).activateClient();
                    }
                }
            }
        }
    }

    boolean isPresent(String id) throws SQLException {
        java.sql.Statement statement = Statement.getStatement();
        ResultSet resultSet;

        System.out.println("Finding whether this person " + id + " has already voted or not");
        resultSet = statement.executeQuery("SELECT voted FROM voter WHERE id = '" + (id).toLowerCase() + "'");
        while (resultSet.next()) {
            if (!resultSet.getString("voted").equals("0")) {
                Statement.closeConnections();
                System.out.println("Returning true");
                return true;
            }
        }

        System.out.println("Returning false");
        Statement.closeConnections();
        return false;
    }

}
