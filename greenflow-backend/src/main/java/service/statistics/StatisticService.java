package service.statistics;

import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import enums.Currency;
import enums.PaymentType;
import rest.Response.StatisticBarResponse;
import rest.Response.StatisticPieResponse;
import rest.Response.StatisticsBalanceResponse;
import service.authentication.LoggedInService;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Model
public class StatisticService {


    @Inject
    LoggedInService loggedInService;

    @Inject
    TransactionDao transactionDao;

    @Inject
    CategoryDao categoryDao;


    public List<StatisticPieResponse> getIncomePieStatistics(int year, int month) throws Exception {
        String username = loggedInService.getCurrentUserName();
        ArrayList<StatisticPieResponse> ret = new ArrayList<>();
        ArrayList<Transaction> transactions = transactionDao.getMonthlyTransactions(username, year, month, false);
        ArrayList<Category> categories = categoryDao.getAllByUsername(username);

        orderingByCurrencyPaymentType(ret, transactions, categories);
        return ret;
    }

    public List<StatisticPieResponse> getExpensePieStatistics(int year, int month) {
        String username = loggedInService.getCurrentUserName();
        ArrayList<StatisticPieResponse> ret = new ArrayList<>();
        ArrayList<Transaction> transactions = transactionDao.getMonthlyTransactions(username, year, month, true);
        ArrayList<Category> categories = categoryDao.getAllByUsername(username);
        orderingByCurrencyPaymentType(ret, transactions, categories);
        return ret;
    }

    public List<StatisticsBalanceResponse> getBalance() throws Exception {
        String username = loggedInService.getCurrentUserName();
        ArrayList<StatisticsBalanceResponse> ret = new ArrayList<>();
        ArrayList<Transaction> transactions = transactionDao.getEntireTransactionHistory(username);
        for (Currency currency : Currency.values()
        ) {
            int sumCard = 0;
            int sumCash = 0;
            boolean asd=false;
            for (Transaction transaction : transactions
            ) {
                if (transaction.getCurrency().equals(currency)) {
                    if (transaction.getPaymentType().equals(PaymentType.CARD)) {
                        if ((transaction.isExpense()) == true) {
                            sumCard-=transaction.getAmmount();
                        } else {
                            sumCard+=transaction.getAmmount();
                        }
                    }
                    if (transaction.getPaymentType().equals(PaymentType.CASH)) {
                        if ((transaction.isExpense()) == true) {
                            sumCash-=transaction.getAmmount();
                        } else {
                            sumCash+=transaction.getAmmount();
                        }
                    }
                }
            }
            StatisticsBalanceResponse element = new StatisticsBalanceResponse();
            element.setCurrency(currency);
            element.setCurrentCardBalance(sumCard);
            element.setCurrentCashBalance(sumCash);
            element.setCurrentTotalBalance(sumCard + sumCash);
            ret.add(element);
        }
        return ret;
    }

    public StatisticBarResponse getBarStatistics(int months, String currency) {
        String username = loggedInService.getCurrentUserName();
        StatisticBarResponse statisticBarResponse = new StatisticBarResponse();
        Currency c= Currency.valueOf(currency);
        statisticBarResponse.setExpenseData(getBarData(username,months,true,Currency.valueOf(currency)));
        statisticBarResponse.setIncomeData(getBarData(username,months,false,Currency.valueOf(currency)));
        statisticBarResponse.setLabels(getBarLabels(months));
        return statisticBarResponse;
    }

    private List<String> getBarLabels(int months) {
        ArrayList<String> ret = new ArrayList<>();
        for (int i = 0; i < months ; i++) {
            LocalDate date = LocalDate.now().minusMonths(i);
            String temp = date.getYear() + "-" + date.getMonthValue();
            ret.add(temp);
        }
        Collections.reverse(ret);
        return ret;
    }

    private List<Integer> getBarData(String username, int months, boolean isExpense, Currency currency) {
        ArrayList <Integer> ret= new ArrayList<>();
        for (int i = 0; i < months; i++) {
            LocalDate date = LocalDate.now().minusMonths(i);
            int sum=0;
            ArrayList<Transaction> transactions = transactionDao.getMonthlyTransactionsWithCurrency(username, date.getYear(), date.getMonthValue(),isExpense,currency);
            for (Transaction transaction : transactions) {
                sum+=transaction.getAmmount();
            }
            ret.add(sum);
        }
        Collections.reverse(ret);
        return ret;
    }

    private void orderingByCurrencyPaymentType(ArrayList<StatisticPieResponse> ret, ArrayList<Transaction> transactions, ArrayList<Category> categories) {
        for (PaymentType ptype : PaymentType.values()
        ) {
            for (Currency currency : Currency.values()) {
                Map<String, Integer> mappedValues = getMappedValues(transactions, categories, ptype, currency);
                StatisticPieResponse statisticPieResponse = getStatisticPieResponse(mappedValues, ptype, currency);
                ret.add(statisticPieResponse);
            }
        }
    }

    private Map<String, Integer> getMappedValues(ArrayList<Transaction> transactions, ArrayList<Category> categories, PaymentType ptype, Currency currency) {
        Map<String, Integer> mappedValues = new HashMap<>();
        for (Category category : categories) {
            int value = 0;
            for (Transaction transaction : transactions) {
                if (transaction.getCategory().equals(category.getName())
                        && transaction.getCurrency().equals(currency)
                        && transaction.getPaymentType().equals(ptype)) {
                    value += transaction.getAmmount();
                }
            }
            mappedValues.put(category.getName(), value);
        }
        return mappedValues;
    }

    private StatisticPieResponse getStatisticPieResponse(Map<String, Integer> mappedValues, PaymentType ptype, Currency currency) {
        StatisticPieResponse statisticPieResponse = new StatisticPieResponse();
        ArrayList listofKeys = mappedValues.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
        ArrayList listofvalues = mappedValues.values().stream().collect(Collectors.toCollection(ArrayList::new));
        statisticPieResponse.setLabels(listofKeys);
        statisticPieResponse.setData(listofvalues);
        statisticPieResponse.setCurrency(currency);
        statisticPieResponse.setPaymentType(ptype);
        if (mappedValues.values().stream().mapToInt(Integer::intValue).sum() == 0) {
            statisticPieResponse.setRelevant(false);
        } else {
            statisticPieResponse.setRelevant(true);
        }
        return statisticPieResponse;
    }


}
