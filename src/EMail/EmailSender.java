/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EMail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Y2K
 */
public class EmailSender {

    public static boolean sendMail(String from, String password, String subject,
                                   String message,
                                   String[] to, String[] cc, String[] bcc) {
        boolean messageSent;
        String host;
        Properties properties;
        Session session;
        Transport transport;
        MimeMessage mimeMessage;

        host = "smtp.gmail.com";
        properties = createAndGetProperties(host, from, password);

        session = Session.getInstance(properties);
        mimeMessage = new MimeMessage(session);

        try {
            mimeMessage.setFrom(new InternetAddress(from));

            mimeMessage.addRecipients(Message.RecipientType.TO, createAndGetAddresses(to));
            mimeMessage.addRecipients(Message.RecipientType.CC, createAndGetAddresses(cc));
            mimeMessage.addRecipients(Message.RecipientType.BCC, createAndGetAddresses(bcc));

            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();

            messageSent = true;
        } catch (MessagingException e) {
            e.printStackTrace();
            messageSent = false;
        }

        return messageSent;
    }

    private static Properties createAndGetProperties(String host,
                                                     String username,
                                                     String password) {
        Properties properties;

        properties = System.getProperties();

        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.user", username);
        properties.put("mail.smtp.password", password);
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", "true");

        return properties;
    }

    private static InternetAddress[] createAndGetAddresses(String[] addresses) throws AddressException {
        int i;
        int length;
        InternetAddress[] internetAddresses;

        if (addresses != null) {
            length = addresses.length;
            internetAddresses = new InternetAddress[length];

            for (i = 0; i < length; i++) {
                internetAddresses[i] = new InternetAddress(addresses[i]);
            }

            return internetAddresses;
        } else {
            return null;
        }
    }

}
