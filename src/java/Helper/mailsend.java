/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author george
 */
public class mailsend {

    public void sendmail(String To, String message) throws NoSuchProviderException, AddressException, MessagingException {
        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        // current system properties.

        Properties mailserverproperties = System.getProperties();
        mailserverproperties.put("mail.transport.protocol", "smtp");
        mailserverproperties.put("mail.smtp.port", "587");
        mailserverproperties.put("mail.smtp.auth", "true");
        mailserverproperties.put("mail.smtp.host", "mail.sbctv.gr");
        mailserverproperties.put("mail.smtp.starttls.enable", "true");
        mailserverproperties.put("mail.smtp.from", "somename@somemail.com");

        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
//   Almost all code should use Session.getInstance.
//   The Session.getDefaultInstance method creates a new Session the first time it's
//   called, using the Properties that are passed.
//   Subsequent calls will return that original Session
//   and ignore any Properties you pass in. If you want to
//   create different Sessions
//   with different properties,
//   Session.getDefaultInstance won't do that.
        Session mailsession = Session.getInstance(mailserverproperties, null);
        MimeMessage mymailmessage = new MimeMessage(mailsession);
        mymailmessage.addRecipient(Message.RecipientType.TO, new InternetAddress(To));
        mymailmessage.setSubject("Info");

        // Object to hold the parts of the body
        Multipart multipart = new MimeMultipart();
        //This class represents a MIME body part. It implements the BodyPart
        //abstract class and the MimePart interface.
        //MimeBodyParts are contained in MimeMultipart objects.
        // One part for text
        MimeBodyPart mimeBodyPartforBody = new MimeBodyPart();
//        File attachement = new File("F:\\2Test.docx");
//        try {
//            mimeBodyPartForAttachment.attachFile(attachement);
//        } catch (IOException ex) {
//            Logger.getLogger(JavaMailExample.class.getName()).log(Level.SEVERE, null, ex);
//        }
        multipart.addBodyPart(mimeBodyPartforBody);
        //    multipart.addBodyPart(mimeBodyPartForAttachment);
        String emailBody = message;
        mimeBodyPartforBody.setText(emailBody);
        mymailmessage.setContent(multipart);

        System.out.println("Mail Session has been created  successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = mailsession.getTransport("smtp");

        //An abstract class that models a message transport.
        transport.connect("somemail.somedomain.gr", "somename@somemail.gr", "somepassword");
        transport.sendMessage(mymailmessage, mymailmessage.getAllRecipients());
        transport.close();
    }
}
