package service.transaction;

import converters.TransactionConverter;
import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import rest.Input.TransactionInput;
import service.authentication.LoggedInService;
import service.budget.BudgetService;
import service.events.EventService;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.ArrayList;

@Model
public class TransactionService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private BudgetService budgetService;


    @Inject
    private TransactionConverter transactionConverter;

    public void post(TransactionInput transactionInput) throws Exception {
        Transaction transaction= transactionConverter.restInputToService(transactionInput,loggedInService.getCurrentUserName());
        transactionDao.post(transaction);
        //Whenever a transaction expense posted, the BudgetService checks, if it overextends any limit.
        budgetService.checkLimitExtension(transaction);
    }



    public ArrayList<Transaction> getTransactions() throws Exception {
        return transactionDao.getEntireTransactionHistory(loggedInService.getCurrentUserName());
    }

    public void delete(int id) throws Exception {
        String username=loggedInService.getCurrentUserName();
        transactionDao.delete(id,username);
    }

    public void modifyTransaction(int id, TransactionInput transactionInput) throws Exception {
        String username=loggedInService.getCurrentUserName();
        Transaction transaction = transactionConverter.restInputToService(transactionInput,loggedInService.getCurrentUserName());
        transaction.setId(id);
        transactionDao.modify(transaction);
    }
}
