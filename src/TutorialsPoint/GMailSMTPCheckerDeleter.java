/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorialsPoint;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

/**
 *
 * @author Y2K
 */
public class GMailSMTPCheckerDeleter {

    private String username;
    private String from;
    private String password;
    private final String FOLDER = "INBOX";
    private final String HOST = "pop.gmail.com";
    private final String PROVIDER = "iiita.ac.in";
    private final String PROTOCOL = "pop3s";
    private final int PORT = 995;

    public void setUsername(String username) {
        this.username = username;
        setFrom(username + "@" + getPROVIDER());
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public String getFrom() {
        return from;
    }

    public String getFOLDER() {
        return FOLDER;
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public String getPROTOCOL() {
        return PROTOCOL;
    }

    public String getPROVIDER() {
        return PROVIDER;
    }

    private Properties createAndGetProperties() {
        Properties properties;

        properties = new Properties();

        properties.put("mail.pop3.host", getHOST());
        properties.put("mail.pop3.port", getPORT());
        properties.put("mail.pop3.starttls.enable", "true");

        return properties;
    }

    public boolean checkAndDeleteMail(String OTP) {
        Properties properties;
        Session session;
        Store store;
        Folder folder;
        Message[] messages;
        int messageCount;
        int i;
        boolean mailChecked;

        mailChecked = false;
        properties = createAndGetProperties();
        session = Session.getInstance(properties);

        try {
            // Connecting to the Mail Server
            store = session.getStore(getPROTOCOL());
            store.connect(getHOST(), getUsername(), getPassword());

            /*            
             // Opening the Inbox
             folder = store.getFolder(getFOLDER());
             folder.open(Folder.READ_WRITE);

             // Retrieving the 11 latest Messages
             messageCount = folder.getMessageCount();
             messages = folder.getMessages(messageCount - 10, messageCount);

             // Checking for OTP Match
             for (i = 10; i >= 0; i--) {
             if (messages[i].getSubject().contains(OTP)) {
             mailChecked = true;
             messages[i].setFlag(Flags.Flag.DELETED, true);
             break;
             }
             }

             */
            // Closing the Folder & Terminating Server Connection
            //folder.close(true);
            store.close();

            return true;
        } catch (NoSuchProviderException noSuchProviderException) {
            System.out.println("\nError! : POP3 Provider Not Found");
        } catch (MessagingException messagingException) {
            System.out.println("\nError! : Unable to Fetch Mail\nException:\t" + messagingException.getMessage());
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            System.out.println("\nError! : Mail not found");
        } catch (Exception exception) {
            System.out.println("\nError! : Unknown Error - " + exception.toString());
        }

        return mailChecked;
    }

    public static void main(String[] args) {
        Scanner scanner;
        Random random;
        int OTP;
        String username;
        String senderPassword;
        String recipientPassword;
        String recipient;

        random = new Random(System.currentTimeMillis());
        scanner = new Scanner(System.in);
        OTP = random.nextInt();
        GMailSMTPSender gMailSMTPSender = new GMailSMTPSender();
        GMailSMTPCheckerDeleter gMailSMTPChecker = new GMailSMTPCheckerDeleter();

        System.out.print("Enter Enrollment No:\t\t");
        username = scanner.nextLine().toLowerCase().trim();

        System.out.print("Enter Sender Password:\t\t");
        senderPassword = scanner.nextLine();

        System.out.print("\nEnter Recipient:\t\t");
        recipient = scanner.nextLine().toLowerCase().trim() + "@iiita.ac.in";

        System.out.print("Enter Recipient Password:\t");
        recipientPassword = scanner.nextLine();

        gMailSMTPSender.setUsername(username);
        gMailSMTPSender.setPassword(senderPassword);
        gMailSMTPSender.setRecipient(recipient);
        gMailSMTPSender.setSubject("OTP for Voting:\t" + Integer.toString(OTP));
        gMailSMTPSender.setMessage("No Message Body Required");

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
            Thread.sleep(5000);
        } catch (InterruptedException interruptedException) {
            System.out.println("Error! : " + interruptedException.getMessage());
        }

        gMailSMTPChecker.setUsername(recipient);
        gMailSMTPChecker.setPassword(recipientPassword);

        try {
            System.out.println("\nChecking OTP via POP3");
            if (gMailSMTPChecker.checkAndDeleteMail(Integer.toString(OTP))) {
                System.out.println("OTP Verified Successfully!");
            } else {
                System.out.println("Sorry, OTP couldn't be verified");
            }
        } catch (Exception exception) {
            System.out.println("\nError! : " + exception.getMessage());
        } finally {
            gMailSMTPChecker = null;
        }
    }

}
