package service.events;


import dao.UserDao;
import service.authentication.User;
import service.budget.Budget;

import java.util.*;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.*;

@Model
public class MailService {

    @Inject
    UserDao userDao;

    static Properties mailServerProperties;
    static Session getMailSession;
    static MimeMessage generateMailMessage;
    static ResourceBundle rb = ResourceBundle.getBundle("emailcredentials");

    public void sendMonthly(String[] addresses) throws AddressException, MessagingException {
        mailPropertiesSetup();


        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress("dummy@gmail.com"));
        generateMailMessage.setSubject("Greetings from Greenflow..");
        String emailBody = "Test mail, checking the integration of the JAVAMAIL into the application.";
        generateMailMessage.setContent(emailBody, "text/html");


        transportMail();
    }

    public void sendWarning(String username, Budget budget)  throws AddressException, MessagingException {
        mailPropertiesSetup();

        User user=new User();
        user.setUsername(username);
        String emailAddress=userDao.getEmail(user);

        if(emailAddress != null) {
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            generateMailMessage.setSubject("Greetings from Greenflow..");
            String emailBody = "This is a warning message, because your expense in "+ budget.getCurrency() + " "
                    +budget.getPaymentType() + " overextended the warning limit: "+ budget.getWarning() + ". Your limit  is:" +
                    budget.getLimit() +
                    "\n Have a good shopping! ~ Greenflow";

            generateMailMessage.setContent(emailBody, "text/html");


            transportMail();
        }
    }

    private void transportMail() throws MessagingException {
        Transport transport = getMailSession.getTransport("smtp");
        String user = rb.getString("username");
        String password = rb.getString("password");
        transport.connect("smtp.gmail.com", user, password);
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    private void mailPropertiesSetup() {
        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", "587");
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");
    }



}
