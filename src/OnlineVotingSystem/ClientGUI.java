/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package OnlineVotingSystem;

import JDBC.Statement;
import TutorialsPoint.GMailSMTPCheckerDeleter;
import TutorialsPoint.GMailSMTPSender;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Orientation;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

/**
 *
 * @author Y2K
 */
public class ClientGUI extends JFrame implements ActionListener, WindowListener {

    // UI Elements
    JPasswordField textFieldPassword;
    JButton buttonLogin;
    JButton buttonSubmit;
    JCheckBox checkBox[];
    JLabel labelUsername;
    JLabel labelInstruction;
    
    // Counters
    int checkBoxTotal;
    int checkBoxMaxChecked;
    int checkBoxCurrentChecked;
    
    // Others
    Client client;
    Random random;
    
    // E-Mail Verification
    GMailSMTPCheckerDeleter gMailSMTPCheckerDeleter;
    GMailSMTPSender gMailSMTPSender;
        
    // Database    
    private java.sql.Statement statement;
    String voterID;
    String collegeID;
    String constituencyName;
    int maxCandidates;
    int currCandidates;
    int selCandidates;
    String candidateID[];
    String candidateName[];
    boolean voteRecieved[];
    
    // Email ID-Passwords
    String senderEmail;
    String recipientEmail;
    String senderPassword;
    String recepientPassword;

    // Normal Execution
    public ClientGUI() {
        textFieldPassword = new JPasswordField();
        buttonLogin = new JButton("Login");
        buttonSubmit = new JButton("Submit");
        labelUsername = new JLabel();
        labelInstruction = new JLabel();
        selCandidates = 0;
        senderEmail = "iec2013006";
        senderPassword = "Hellboy1";
        random = new Random();
    }

    public static void main(String[] args) {
        ClientGUI clientGUI;

        clientGUI = new ClientGUI();

        clientGUI.createCheckBoxes();
        clientGUI.createFrame();
        clientGUI.addListeners();

        try {
            clientGUI.changeVotingStatus(false);
            clientGUI.changeLoginStatus(false);
            clientGUI.connect();
        } catch (IOException iOException) {
            System.out.println("Error:\t" + iOException.getMessage());
        }
    }

    public void connect() throws IOException {
        int port;
        String host;
        Socket socket;
        PrintWriter printWriter;
        Scanner scanner;
        Thread thread;

        port = 444;
        host = "localhost";
        socket = new Socket(host, port);
        //printWriter = new PrintWriter(socket.getOutputStream());
        scanner = new Scanner(socket.getInputStream());

        System.out.println("Connected to host:\t" + socket.getLocalAddress().getHostName());
        System.out.println("Waiting for Agent to activate..");

        client = new Client(socket);
        try {
            while (socket.isConnected()) {
                try {
                    selCandidates = 0;
                    labelUsername.setText(scanner.nextLine());
                    //System.out.println("Scanned line:\t" + labelUsername.getText() + " from Client");
                    changeLoginStatus(true);
                    //thread = new Thread(client);
                    //thread.start();
                } catch (Exception exception) {
                    //thread = null;
                    System.out.println("Error while recieving from agent:\t" + exception);
                    return;
                }

                try {
                    //thread.join();
                } catch (Exception exception) {
                    System.out.println("Error:\t" + exception.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Host disconnected:\t" + e.getMessage());
        }
    }

    // GUI Deisigning
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
        JPanel panelLoginLabels;
        JPanel panelLoginFields;
        JPanel panelUpper;
        JLabel labelUsername;
        JLabel labelPassword;

        labelUsername = new JLabel("Username");
        labelPassword = new JLabel("Password");

        panelLoginLabels = wrapComponents(new Component[]{labelUsername, labelPassword}, Orientation.VERTICAL);
        panelLoginFields = wrapComponents(new Component[]{this.labelUsername, textFieldPassword}, Orientation.VERTICAL);
        //panelCheckBoxes = wrapComponents(checkBox, Orientation.VERTICAL);

        panelUpper = new JPanel(new BorderLayout(10, 10));
        //panelLower = new JPanel(new BorderLayout(10, 10));

        panelUpper.add(panelLoginLabels, BorderLayout.WEST);
        panelUpper.add(panelLoginFields, BorderLayout.CENTER);
        panelUpper.add(buttonLogin, BorderLayout.SOUTH);

        //panelLower.add(labelInstruction, BorderLayout.NORTH);
        //panelLower.add(panelCheckBoxes, BorderLayout.CENTER);
        //panelLower.add(buttonSubmit, BorderLayout.SOUTH);
        add(panelUpper, BorderLayout.NORTH);
        //add(panelLower, BorderLayout.CENTER);
    }

    public void createFrame() {
        setSize(300, 320);
        setTitle("Client Window");
        setLocation(500, 250);
        setLayout(new BorderLayout());

        addComponents();

        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Event Handling (ActionListener and other supporting methods)
    public void addListeners() {
        buttonLogin.addActionListener(this);
        buttonSubmit.addActionListener(this);
        addWindowListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int i;
        boolean voteSuccessful;

        if (e.getSource() == buttonLogin) {
            // source of click was login button
            if (!textFieldPassword.getText().isEmpty()) {
                voterID = labelUsername.getText().toLowerCase();
                recipientEmail = voterID + "@iiita.ac.in";
                recepientPassword = textFieldPassword.getText();
                if (sendAndVerifyMail()) {
                    System.out.println("\n=====\nAuthentication successful!\n=====");
                    changeLoginStatus(false);

                    try {
                        statement = Statement.getStatement();
                        fetchCollegeIDAndConstituency();
                        fetchMaxAndCurrCandidates();
                        fetchCandidateList();
                        createCheckBoxes();
                        changeVotingStatus(true);
                    } catch (SQLException sQLException) {
                        System.out.println("Exception while fetching voter details:\t" + sQLException.getMessage());
                    } finally {
                        try {
                        Statement.closeConnections();
                        } catch (SQLException sQLException) {
                          System.out.println("\nError while closing statement:\t" + sQLException.getMessage());
                        }
                    }
                } else {
                    textFieldPassword.setText("");
                    System.out.println("\n\n=====\nERROR : EMAIL COULDN'T BE VERIFIED\nPLEASE TRY AGAIN\n=====\n\n");
                }
            }
        } else if (e.getSource() == buttonSubmit) {
            // source of click was submit button
            clearLogin();
            voteSuccessful = true;

            try {
                statement = Statement.getStatement();
                updateCandidateVote();
                updateVoter();
            } catch (SQLException sQLException) {
                voteSuccessful = false;
                System.out.println("SQL Exception while voting:\t" + sQLException.getMessage());
            } finally {
                if (voteSuccessful) {
                    System.out.println("Voting Done Called");
                    client.votingDone();
                    changeLoginStatus(true);
                    changeVotingStatus(false);
                    clearCandidates();
                }
                try {
                    Statement.closeConnections();
                } catch (SQLException ex) {
                    System.out.println("\nError while closing database connection\n");
                }
            }
        }

        for (i = 0; i < currCandidates; i++) {
            if (e.getSource() == checkBox[i]) {
                // source of click was one of the check-boxes
                if (checkBox[i].isSelected()) {
                    if (selCandidates < maxCandidates) {
                        ++selCandidates;
                        voteRecieved[i] = true;
                    } else {
                        // this is done to prevent selection of more than max no of candidates that could be voted for
                        checkBox[i].setSelected(false);
                    }
                } else {
                    // if user clicks on a check box already selected, then it gets unselected and vote to that candidate is removed
                    --selCandidates;
                    voteRecieved[i] = false;
                }
            }
        }
    }

    public void changeVotingStatus(boolean status) {
        // if status is true, it enables checkboxes and submit button to allow user to vote, otherwise it disables them
        int i;

        buttonSubmit.setEnabled(status);
        for (i = 0; i < checkBoxTotal; i++) {
            checkBox[i].setEnabled(status);
        }
    }

    public void changeLoginStatus(boolean status) {
        if (labelUsername.getText().isEmpty()) {
            // If username label is blank, i.e., agent hasn't activated this client to vote, then it disables all components
            // in window like login button and password field
            changeVotingStatus(false);
            textFieldPassword.setEnabled(false);
            buttonLogin.setEnabled(false);
        } else {
            // otherwise, it clears the password field
            textFieldPassword.setText("");
            
            // and if status is true, it enables password field and login button, otherwise it disables them
            textFieldPassword.setEnabled(status);
            buttonLogin.setEnabled(status);
        }
    }

    public void clearLogin() {
        labelUsername.setText("");
        textFieldPassword.setText("");
    }

    public void clearCandidates() {
        int i;

        buttonSubmit.setEnabled(false);
        for (i = 0; i < currCandidates; i++) {
            remove(checkBox[i]);
        }
        validate();
    }

    // EMail Verification
    private boolean sendAndVerifyMail() {
        int OTP;

        gMailSMTPSender = new GMailSMTPSender();
        gMailSMTPCheckerDeleter = new GMailSMTPCheckerDeleter();

        OTP = random.nextInt();
        gMailSMTPSender.setUsername(senderEmail);
        gMailSMTPSender.setPassword(senderPassword);
        gMailSMTPSender.setRecipient(recipientEmail);
        gMailSMTPSender.setSubject("OTP For Voting: " + OTP);
        gMailSMTPSender.setMessage("This is an auto-generated E-Mail.\nPlease do not reply to this message.");

        try {
            System.out.println("\nSending OTP via Email");
            if (gMailSMTPSender.sendMail()) {
                System.out.println("Email Sent Successfully!");
            } else {
                System.out.println("Sorry, Email couldn't be sent");
            }
        } catch (Exception exception) {
            System.out.println("\nError! : " + exception.getMessage());
        } finally {
            gMailSMTPSender = null;
        }

        try {
            System.out.println("\nWaiting for delivery...");
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            System.out.println("Error! : " + interruptedException.getMessage());
        }

        //System.out.println("\nFor Voter " + voterID + ", recipientEmail = " + recipientEmail + " and password = " + recepientPassword + "\n");
        gMailSMTPCheckerDeleter.setUsername(recipientEmail);
        gMailSMTPCheckerDeleter.setPassword(recepientPassword);

        try {
            System.out.println("\nChecking OTP via POP3");
            if (gMailSMTPCheckerDeleter.checkAndDeleteMail(Integer.toString(OTP))) {
                System.out.println("OTP Verified Successfully!");
                return true;
            } else {
                System.out.println("Sorry, OTP couldn't be verified");
                return false;
            }
        } catch (Exception exception) {
            System.out.println("\nError! : " + exception.getMessage());
            return false;
        } finally {
            gMailSMTPCheckerDeleter = null;
        }
    }

    // Normal Voting Process Methods
    private void fetchCollegeIDAndConstituency() throws SQLException {
        ResultSet resultSet;

        System.out.println("\n\n=====\nVOTING PROCESS STARTED\n=====\n");
        System.out.println("Fetching CollegeID and Constituency for voter " + voterID);
        resultSet = statement.executeQuery("SELECT college_id, constituency_name FROM voter WHERE id = '" + voterID + "'");
        while (resultSet.next()) {
            collegeID = resultSet.getString("college_id");
            constituencyName = resultSet.getString("constituency_name");
        }
    }

    private void fetchMaxAndCurrCandidates() throws SQLException {
        ResultSet resultSet;

        System.out.println("\nFetching current and maximum no of candidates for voter " + voterID);
        resultSet = statement.executeQuery("SELECT maximum_representative, current_representative FROM constituency WHERE college_id = '" + collegeID + "' AND name_constituency = '" + constituencyName + "'");
        while (resultSet.next()) {
            maxCandidates = Integer.parseInt(resultSet.getString("maximum_representative"));
            currCandidates = Integer.parseInt(resultSet.getString("current_representative"));
        }

        candidateID = new String[currCandidates];
        candidateName = new String[currCandidates];
        voteRecieved = new boolean[currCandidates];

        labelInstruction.setText("You can select upto " + maxCandidates + " from following list");
    }

    private void fetchCandidateList() throws SQLException {
        ResultSet resultSet;
        int i;

        System.out.println("\nFetching candidate list for voter " + voterID);
        resultSet = statement.executeQuery("SELECT id, name_candidate FROM candidate WHERE college_id = '" + collegeID + "' AND constituency_name = '" + constituencyName + "'");
        i = 0;

        while (resultSet.next()) {
            candidateID[i] = resultSet.getString("id");
            candidateName[i] = resultSet.getString("name_candidate");
            i++;
        }
    }

    private void createCheckBoxes() {
        int i;
        JPanel panelCheckBoxes;
        JPanel panelLower;

        checkBoxTotal = currCandidates;
        checkBoxMaxChecked = maxCandidates;
        checkBoxCurrentChecked = 0;

        checkBox = new JCheckBox[checkBoxTotal];

        System.out.println("\nCreating candidate list for voter " + voterID);
        for (i = 0; i < checkBoxTotal; i++) {
            checkBox[i] = new JCheckBox(candidateName[i]);
            checkBox[i].addActionListener(this);
        }

        panelCheckBoxes = wrapComponents(checkBox, Orientation.VERTICAL);
        panelLower = new JPanel(new BorderLayout(10, 10));
        panelLower.add(labelInstruction, BorderLayout.NORTH);
        panelLower.add(panelCheckBoxes, BorderLayout.CENTER);
        panelLower.add(buttonSubmit, BorderLayout.SOUTH);
        add(panelLower, BorderLayout.CENTER);
    }

    private void updateCandidateVote() throws SQLException {
        int i;

        System.out.println("\nUpdating vote count of candidates chosen by voter " + voterID);
        for (i = 0; i < currCandidates; i++) {
            //System.out.println("In loop");
            if (voteRecieved[i]) {
                //System.out.println("Inside if before update");
                statement.executeUpdate("UPDATE candidate SET no_of_votes = no_of_votes + 1 WHERE id = '" + candidateID[i] + "'");
                //System.out.println("Inside if after updatea");
            }
        }
    }

    private void updateVoter() throws SQLException {
        System.out.println("\nUpdating voting history of voter " + voterID);
        statement.executeUpdate("UPDATE voter SET voted = 1 WHERE id = '" + voterID + "'");
    }

    // Event Handling (WindowListener), only windowClosing is used (if possible, should've used some adapter)
    @Override
    public void windowOpened(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowClosing(WindowEvent e) {
        try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            client.votingDone();
            Statement.closeConnections();
        } catch (SQLException ex) {
            Logger.getLogger(ClientGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowIconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowActivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
