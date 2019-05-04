package service.events;


import dao.UserDao;
import service.authentication.User;
import service.budget.Budget;
import service.transaction.Transaction;

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

    public void sendMonthly(List<Transaction> transactions) throws AddressException, MessagingException {
        if(!transactions.isEmpty()) {

            mailPropertiesSetup();
            String emailAddress = userDao.getEmail(transactions.get(0).getUsername());
            getMailSession = Session.getDefaultInstance(mailServerProperties, null);
            generateMailMessage = new MimeMessage(getMailSession);
            generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            generateMailMessage.setSubject("Greenflow monthly report message");

            StringBuilder sb = new StringBuilder();
            sb.append("<html><body>");
            sb.append("Your monthly report consisted the following things");
            sb.append("<table>");
            sb.append(" <tr>\n" +
                    "    <th>Name</th>\n" +
                    "    <th>Ammount</th> \n" +
                    "    <th>Currency</th>\n" +
                    "    <th>Category</th>\n" +
                    "    <th>PaymentType</th>\n" +
                    "    <th>Date</th>\n" +
                    "    <th>Exp/Inc</th>\n" +
                    "  </tr>\n");
            for (Transaction t : transactions
            ) {
                sb.append("<tr>");
                sb.append("<td>" + t.getName() + "</td>");
                sb.append("<td>" + t.getAmmount() + "</td>");
                sb.append("<td>" + t.getCurrency() + "</td>");
                sb.append("<td>" + t.getCategory() + "</td>");
                sb.append("<td>" + t.getPaymentType() + "</td>");
                sb.append("<td>" + t.getDate().getYear() + "-" + t.getDate().getMonth() + "-" + t.getDate().getDayOfMonth() + "</td>");
                if (t.isExpense()) {
                    sb.append("<td>Expense</td>");
                } else {
                    sb.append("<td>Income</td>");
                }
                sb.append("</tr>");
            }
            sb.append("</table>");
            sb.append("For more detailed statistics, visit the webpage");
            sb.append("</body></html>");
            String emailBody = sb.toString();

            generateMailMessage.setContent(emailBody, "text/html; charset=utf-8");


            transportMail();
        }
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
            generateMailMessage.setSubject("Greenflow budget warning message");
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
