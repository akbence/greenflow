package service.transaction;

import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import enums.Currency;
import enums.PaymentType;
import rest.Input.CategoryInput;
import rest.Input.TransactionInput;
import service.authentication.LoggedInService;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Model
public class TransactionService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private TransactionDao transactionDao;

    @Inject
    CategoryDao categoryDao;

    public void post(TransactionInput transactionInput) throws Exception {
        if (loggedInService.isLoggedIn()) {
            loggedInService.checkToken(transactionInput.getToken());

            Transaction transaction = new Transaction();
            transaction.setAmmount(transactionInput.getAmount());
            transaction.setExpense(transactionInput.isExpense());
            transaction.setName(transactionInput.getName());
            transaction.setCategory(transactionInput.getCategory());

            transaction.setDate(stringToDate(transactionInput.getDate()));
            transaction.setPaymentType(stringToPayment(transactionInput.getPaymentType()));
            transaction.setCurrency(stringToCurrency(transactionInput.getCurrency()));

            transactionDao.post(transaction);

            // TODO: implement the rest of the service
            System.out.println("user logged in");
            System.out.println(loggedInService.getCurrentUserName());

        }

    }

    private PaymentType stringToPayment(String paymentType) throws Exception {

        if (paymentType.equals("CASH")) {
            return PaymentType.CASH;
        } else if (paymentType.equals("CARD")) {
            return PaymentType.CARD;
        } else {
            throw new Exception("payment type invalid");
        }
    }

    private LocalDate stringToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }

    private Currency stringToCurrency(String currency){
        return Currency.valueOf(currency);
    }
}
