/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EMail;

/**
 *
 * @author Y2K
 */
public class EMailDemo {

    public static void main(String[] args) {
        String from = "iit2013180@iiita.ac.in";
        String password = getPassword();
        String subject = "Sample E-Mail";
        String message = "This mail has been sent using Java Mail APIs";
        String[] to = new String[] {"y2k.shubhamgupta@gmail.com"};
        String[] cc = new String[] {"y2k.shubhamgupta@outlook.com"};
        String[] bcc = new String[] {"y2k_shubhamgupta@operamail.com"};

        boolean messageSent = EmailSender.sendMail(from, password, subject, message, to, cc, bcc);

        if (messageSent) {
            System.out.println("EMail Sent Successfully!\n:-)");
        } else {
            System.out.println("ERROR! : Couldn't Send EMail");
        }
    }

    private static String getPassword() {
        return "Hellboy1";
    }
    
}
