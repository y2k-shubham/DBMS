/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineVotingSystem;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import javafx.geometry.Orientation;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Y2K
 */
public class PollingAgentGUI extends JFrame {

    JTextField textFieldAgentID;
    JPasswordField textFieldPassword;
    JTextField textFieldClient[];
    JButton buttonLogin;
    JButton buttonClient[];
    int maxNumberOfClients;

    public PollingAgentGUI() throws HeadlessException {
        int i;

        maxNumberOfClients = 3;
        textFieldAgentID = new JTextField();
        textFieldPassword = new JPasswordField();
        buttonLogin = new JButton("Login");
        textFieldClient = new JTextField[maxNumberOfClients];
        buttonClient = new JButton[maxNumberOfClients];

        for (i = 0; i < maxNumberOfClients; i++) {
            textFieldClient[i] = new JTextField();
            buttonClient[i] = new JButton("Activate");
        }
    }

    private JPanel wrapComponents(Component[] components, Orientation orientation) {
        int i;
        JPanel jp;

        if (orientation.equals(Orientation.VERTICAL)) {
            jp = new JPanel(new GridLayout(components.length, 1));
        } else {
            jp = new JPanel(new GridLayout(1, components.length));
        }

        for (i = 0; i < components.length; i++) {
            jp.add(components[i]);
        }

        return jp;
    }

    private void addComponents() {
        JPanel jPanelLogin;
        JPanel jPanelClientFields;
        JPanel jPanelClientButtons;
        JPanel jPanelLabels;
        JPanel jPanelUpper;
        JPanel jPanelLower;
        JLabel jLabelUsername;
        JLabel jLabelPassword;

        jLabelUsername = new JLabel("Client ID");
        jLabelPassword = new JLabel("Password");

        jPanelLabels = wrapComponents(new Component[]{jLabelUsername, jLabelPassword}, Orientation.VERTICAL);
        jPanelLogin = wrapComponents(new Component[]{textFieldAgentID, textFieldPassword}, Orientation.VERTICAL);
        jPanelClientFields = wrapComponents(textFieldClient, Orientation.VERTICAL);
        jPanelClientButtons = wrapComponents(buttonClient, Orientation.VERTICAL);

        jPanelLower = new JPanel(new BorderLayout(10, 10));
        jPanelUpper = new JPanel(new BorderLayout(10, 10));

        jPanelUpper.add(jPanelLabels, BorderLayout.WEST);
        jPanelUpper.add(jPanelLogin, BorderLayout.CENTER);
        jPanelUpper.add(buttonLogin, BorderLayout.EAST);

        jPanelLower.add(jPanelClientFields, BorderLayout.CENTER);
        jPanelLower.add(jPanelClientButtons, BorderLayout.EAST);

        add(jPanelUpper, BorderLayout.NORTH);
        add(jPanelLower, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        PollingAgentGUI pollingAgentGUI;

        pollingAgentGUI = new PollingAgentGUI();

        pollingAgentGUI.createFrame();
    }

    public void createFrame() {
        setSize(300, 160);
        setTitle("Agent Dashboard");
        setLocation(500, 250);
        setLayout(new BorderLayout());

        addComponents();
        changeButtonState(false);
        changeLoginState(true);
        changeClientState(false);

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // sets all client activation buttons to specified state
    // (the changeClientState() method also modifies client text fields state)
    public void changeButtonState(boolean state) {
        int i;

        //buttonLogin.setEnabled(false);
        for (i = 0; i < maxNumberOfClients; i++) {
            buttonClient[i].setEnabled(state);
        }
    }

    // disables text fields for agent's ID and password once he has logged in
    public void fixLogin() {
        textFieldAgentID.setEnabled(false);
        textFieldPassword.setEnabled(false);
    }
    
    // enables text fields for agent's ID and password once he has logged out
    public void clearLogin() {
        textFieldAgentID.setText("");
        textFieldPassword.setText("");
    }

    // clears text fields of all voters to blank without enabling or disabling them
    public void clearClients() {
        int i;

        for (i = 0; i < maxNumberOfClients; i++) {
            textFieldClient[i].setText("");
        }
    }

    // modifies enabled state of text fields for agent id and password
    public void changeLoginState(boolean state) {
        // this method works in reverse of login status. if agent has logged in, the username and password fields have to be disabled
        // (unless the agent logs out), that's what it does. similarly, if agent has logged out, fields are enabled
        
        textFieldAgentID.setEnabled(state);
        textFieldPassword.setEnabled(state);
    }

    // sets all client text fields and also activation buttons to specified state
    // (the changeButtonState() method only modifies client activation button states)
    public void changeClientState(boolean state) {
        int i;

        for (i = 0; i < maxNumberOfClients; i++) {
            textFieldClient[i].setEnabled(state);
            buttonClient[i].setEnabled(state);
        }
    }

    // modifies status of a particular client but to specified state.
    // if the client is enabled, the text field for his roll no is cleared after enabling it
    public void changeClientStatus(int index, boolean status) {
        textFieldClient[index].setEnabled(status);
        buttonClient[index].setEnabled(status);
        
        if (status) {
            textFieldClient[index].setText("");
        }
    }
}
