package service.budget;

import converters.BudgetConverter;
import dao.BudgetDao;
import dao.transactions.TransactionDao;
import enums.Currency;
import enums.PaymentType;
import rest.Request.BudgetRequest;
import rest.Response.BudgetResponse;
import service.authentication.LoggedInService;
import service.events.EventService;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Model
public class BudgetService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private EventService eventService;

    @Inject
    private BudgetDao budgetDao;

    @Inject
    private TransactionDao transactionDao;

    @Inject
    private BudgetConverter budgetConverter;

    public void addBudget(BudgetRequest budgetInput) {
        Budget budget = new Budget();
        budget.setUsername(loggedInService.getCurrentUserName());
        budget.setLimit(budgetInput.getLimit());
        budget.setPeriod(LocalDate.now());
        budget.setCurrency(budgetInput.getCurrency());
        budget.setPaymentType(budgetInput.getPaymentType());
        budgetDao.addBudget(budget);
    }

    public List<BudgetResponse> getAllBudget(String year,String month) throws Exception {
        List<Budget> budgetList;
        String username = loggedInService.getCurrentUserName();
        if(month==null ||year == null){
            //Dead code, in theory, it can never reach this part
            budgetList = budgetDao.getAllBudget(username);
        }else{
            LocalDate period= LocalDate.of(Integer.parseInt(year),Integer.parseInt(month),1);
            budgetList = budgetDao.getMonthlyBudget(username,period);
            for (Budget budget: budgetList
            ) {ArrayList<Transaction> transactions = transactionDao.getMonthlyTransactions(username,Integer.parseInt(year),Integer.parseInt(month),true,budget.getCurrency(),budget.getPaymentType());
            int spentMoney=calcSum(transactions);
                for (Transaction t: transactions
                     ) {spentMoney +=t.getAmmount();
                }
                budget.setSpent(spentMoney);
                budget.setPercent(100.0*spentMoney/budget.getLimit());
            }
        }
        return budgetConverter.serviceToRestList(budgetList);
    }


    public void deleteBudget(int id) {
        String username = loggedInService.getCurrentUserName();
        budgetDao.deleteBudget(id,username);

    }

    public void modifyBudgetLimit(int id, int limit) {
        String username = loggedInService.getCurrentUserName();
        budgetDao.modifyBudgetLimit(id,limit,username);
    }

    public void modifyBudgetWarning(int id, int warningLimit) {
        String username = loggedInService.getCurrentUserName();
        budgetDao.modifyBudgetWarning(id,warningLimit,username);
    }

    public void checkLimitExtension(Transaction transaction) throws Exception{
        String username = loggedInService.getCurrentUserName();
        List<Budget> budgets = budgetDao.getMonthlyBudget(username, transaction.getDate());
        PaymentType paymentType = transaction.getPaymentType();
        Currency currency = transaction.getCurrency();
        for (Budget budget : budgets){
            if(budget.getPaymentType().equals(paymentType)
                    && budget.getCurrency().equals(currency)){
                ArrayList<Transaction> monthlyResult = transactionDao.getMonthlyTransactions(username, transaction.getDate().getYear(), transaction.getDate().getMonthValue(), true, currency, paymentType);
                int sum = calcSum(monthlyResult);
                if(isLatestTransactionOverTheValue(transaction,budget.getWarning(),sum)){
                    eventService.sendLimitWarning(budget);
                }
            }
        }
    }

    private int calcSum(ArrayList<Transaction> monthlyResult) {
        int sum = 0;
        for (Transaction t : monthlyResult){
            sum+=t.getAmmount();
        }
        return sum;
    }

    private boolean isLatestTransactionOverTheValue(Transaction transaction, int value, int sum) {
        if(sum >= value){
            sum -= transaction.getAmmount();
            if(sum < value){
                return true;
            }
        }
        return false;
    }
}
