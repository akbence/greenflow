package service.transaction;

import rest.Input.TransactionInput;
import service.authentication.LoggedInService;
import service.authentication.User;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class TransactionService {


    @Inject
    private LoggedInService loggedInService;

    public void post(TransactionInput transactionInput) throws Exception {
        if (loggedInService.isLoggedIn()){
            loggedInService.checkToken(transactionInput.getToken());

            Transaction transaction = new Transaction();
            transaction.setAmmount(transactionInput.getAmount());
            transaction.setCategory(transaction.getCategory());
            transaction.setCurrency(transaction.getCurrency());
            transaction.setExpense(transactionInput.isExpense());
            transaction.setName(transactionInput.getName());
//TODO: setDate
//            transaction.setDate();
//TODO: setPaymentType
//            transaction.setPaymentType(transactionInput.getPaymentType());

            //TODO: implement the rest of the service
            System.out.println("user logged in");
            System.out.println(loggedInService.getCurrentUserName());

        }


    }
}
