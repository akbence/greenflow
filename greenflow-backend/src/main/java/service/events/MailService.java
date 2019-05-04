package service.events;


import java.util.*;
import javax.enterprise.inject.Model;
import javax.mail.*;
import javax.mail.internet.*;

@Model
public class MailService {

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    static ResourceBundle rb = ResourceBundle.getBundle("emailcredentials");

    public static void main(String[] args) throws AddressException, MessagingException {
        // Step1
        System.out.println("\n 1st ===> setup Mail Server Properties..");
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
        System.out.println("Mail Server Properties have been setup successfully..");

        // Step2
        System.out.println("\n\n 2nd ===> get Mail Session..");
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("dummy@gmail.com"));
        generateMailMessage.setSubject("Greetings from Greenflow..");
        String emailBody = "Test mail, checking the integration of the JAVAMAIL into the application.";
        generateMailMessage.setContent(emailBody, "text/html");
        System.out.println("Mail Session has been created successfully..");

        // Step3
        System.out.println("\n\n 3rd ===> Get Session and Send mail");
        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password

        String user = rb.getString("user");
        String password = rb.getString("password");


        transport.connect("smtp.gmail.com", user, password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();


    }
}
