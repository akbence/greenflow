package service.export;

import dao.transactions.TransactionDao;
import rest.Input.ExportInput;
import service.authentication.LoggedInService;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.ArrayList;

@Model
public class ExportService {

    @Inject
    TransactionDao transactionDao;

    @Inject
    LoggedInService loggedInService;

    public void export(ExportInput exportInput) throws Exception {
        if (loggedInService.isLoggedIn()) {
            loggedInService.checkToken(exportInput.getToken());
            try {
                ArrayList<Transaction> transactions = transactionDao.getTransactions(exportInput.getUsername());
                //TODO:implement csv generation
                //return createCsv(transactions);
            } catch (Exception e) {
                throw e;
            }

        }
    }
}
