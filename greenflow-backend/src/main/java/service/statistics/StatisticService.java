package service.statistics;

import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import enums.Currency;
import enums.PaymentType;
import rest.Response.StatisticPieResponse;
import service.authentication.LoggedInService;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
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
        if(mappedValues.values().stream().mapToInt(Integer::intValue).sum() == 0){
            statisticPieResponse.setRelevant(false);
        }else {
            statisticPieResponse.setRelevant(true);
        }
        return statisticPieResponse;
    }
}
