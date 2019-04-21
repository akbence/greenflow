package service.statistics;

import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import rest.Response.StatisticPieResponse;
import service.authentication.LoggedInService;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.lang.reflect.Array;
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


    public StatisticPieResponse getIncomePieStatistics(int year, int month) throws Exception {
        String username=loggedInService.getCurrentUserName();
        ArrayList<Transaction> transactions = transactionDao.getMonthlyTransactions(username, year, month, false);
        ArrayList<Category> categories = categoryDao.getAllByUsername(username);
        Map<String, Integer> mappedValues = getMappedValues(transactions, categories);
        return getStatisticPieResponse(mappedValues);
    }

    private Map<String, Integer> getMappedValues(ArrayList<Transaction> transactions, ArrayList<Category> categories) {
        Map<String,Integer> mappedValues= new HashMap<>();
        for (Category category : categories) {
            int value = 0;
            for (Transaction transaction : transactions) {
                if(transaction.getCategory().equals(category.getName())){
                    value += transaction.getAmmount();
                }
            }
            mappedValues.put(category.getName(),value);
        }
        return mappedValues;
    }

    private StatisticPieResponse getStatisticPieResponse(Map<String, Integer> mappedValues) {
        StatisticPieResponse statisticPieResponse = new StatisticPieResponse();
        ArrayList listofKeys = mappedValues.keySet().stream().collect(Collectors.toCollection(ArrayList::new));
        ArrayList listofvalues= mappedValues.values().stream().collect(Collectors.toCollection(ArrayList::new));
        statisticPieResponse.setLabels(listofKeys);
        statisticPieResponse.setData(listofvalues);
        return statisticPieResponse;
    }
}
