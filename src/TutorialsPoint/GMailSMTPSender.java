/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TutorialsPoint;

import java.util.Properties;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Y2K
 */
public class GMailSMTPSender {

    private String username;
    private String from;
    private String password;
    private String recipient;
    private String subject;
    private String message;
    private final String PROTOCOL = "smtp";
    private final String PROVIDER = "iiita.ac.in";
    private final String HOST = "smtp.gmail.com";
    private final int PORT = 587;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setUsername(String username) {
        this.username = username;
        setFrom(username + "@" + getPROVIDER());
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPROTOCOL() {
        return PROTOCOL;
    }

    public String getPROVIDER() {
        return PROVIDER;
    }

    public String getHOST() {
        return HOST;
    }

    public int getPORT() {
        return PORT;
    }

    public String getMessage() {
        return message;
    }

    public String getPassword() {
        return password;
    }

    public String getSubject() {
        return subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getUsername() {
        return username;
    }

    public String getFrom() {
        return from;
    }

    public boolean sendMail() throws Exception {
        Properties properties;
        Session session;
        MimeMessage mimeMessage;
        Transport transport;

        properties = createAndGetProperties();
        session = Session.getInstance(properties);
        mimeMessage = new MimeMessage(session);
        try {
            transport = session.getTransport(getPROTOCOL());
        } catch (NoSuchProviderException e) {
            System.out.println("\nError! : SMTP Provider not found");
            return false;
        }

        mimeMessage.setFrom(getFrom());
        mimeMessage.addRecipients(Message.RecipientType.TO, getRecipient());
        mimeMessage.setSubject(getSubject());
        mimeMessage.setText(getMessage());

        try {
            transport.connect(getHOST(), getFrom(), getPassword());
        } catch (MessagingException e) {
            System.out.println("\nError! : Unable to authenticate User");
            return false;
        }

        try {
            transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
        } catch (MessagingException e) {
            System.out.println("\nError! : Unable to send Mail\nError:\t" + e.getMessage());
            return false;
        }

        try {
            transport.close();
        } catch (MessagingException e) {
            System.out.println("Unable to close Transport");
        } finally {
            return true;
        }
    }

    private Properties createAndGetProperties() {
        Properties properties;

        properties = System.getProperties();

        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.host", getHOST());
        properties.put("mail.smtp.user", getFrom());
        properties.put("mail.smtp.password", getPassword());
        properties.put("mail.smtp.port", getPORT());

        return properties;
    }

    public static void main(String[] args) {
        Scanner scanner;
        String username;
        String password;
        String recipient;
        String subject;
        String message;

        scanner = new Scanner(System.in);
        GMailSMTPSender gMailSMTP = new GMailSMTPSender();

        System.out.print("Enter Enrollment No:\t");
        username = scanner.nextLine().toLowerCase().trim();

        System.out.print("Enter Password:\t\t");
        password = scanner.nextLine();

        System.out.print("Enter Recipient:\t");
        recipient = scanner.nextLine().toLowerCase().trim();

        System.out.print("Enter Subject:\t\t");
        subject = scanner.nextLine();

        System.out.print("Enter Message:\t\t");
        message = scanner.nextLine();

        gMailSMTP.setUsername(username);
        gMailSMTP.setPassword(password);
        gMailSMTP.setRecipient(recipient);
        gMailSMTP.setSubject(subject);
        gMailSMTP.setMessage(message);

        /*
        System.out.println("\nThe details are:-");
        System.out.println("Username:\t'" + gMailSMTP.getUsername() + "'");
        System.out.println("Password:\t'" + gMailSMTP.getPassword() + "'");
        System.out.println("From:\t\t'" + gMailSMTP.getFrom() + "'");
        System.out.println("To:\t\t'" + gMailSMTP.getRecipient() + "'");
        System.out.println("Subject:\t'" + gMailSMTP.getSubject() + "'");
        System.out.println("Message:\t'" + gMailSMTP.getMessage() + "'");
        */
        
        try {
            System.out.println("\nSending Email");
            if (gMailSMTP.sendMail()) {
                System.out.println("Message Sent Successfully!");
            } else {
                System.out.println("Sorry, Message couldn't be Sent");
            }
        } catch (Exception e) {
            System.out.println("\nError! : " + e.getMessage());
        }
    }

}
