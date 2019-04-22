package converters;

import dao.UserDao;
import dao.transactions.CategoryDao;
import entities.transactions.TransactionEntity;
import enums.Currency;
import enums.PaymentType;
import rest.Input.TransactionInput;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

@Model
public class TransactionConverter {

    @Inject
    UserDao userDao;

    @Inject
    CategoryDao categoryDao;

    /**
     * Usefull and optimized for getting the transactions as a list. It caches all the previously queried IDs for category and username
     * @param entityList
     * @return
     */
    public ArrayList<Transaction> daoToServiceList(ArrayList<TransactionEntity> entityList) {
        ArrayList<Transaction> transactions = new ArrayList();
        HashMap<Integer, String> usersWithIdCache = new HashMap<>();
        HashMap<Integer, String> categoriesWithIdCache = new HashMap<>();
        entityList.forEach(transactionEntity -> transactions.add(daoToService(transactionEntity, usersWithIdCache,categoriesWithIdCache )));
        return transactions;
    }

    public Transaction daoToService(TransactionEntity transactionEntity, HashMap<Integer, String> usersWithIdCache, HashMap<Integer, String> categoriesWithIdCache ) {
        Transaction result = new Transaction();
        result.setPaymentType(transactionEntity.getPaymentType());
        result.setName(transactionEntity.getName());
        result.setExpense(transactionEntity.isExpense());
        result.setCurrency(transactionEntity.getCurrency());
        result.setAmmount(transactionEntity.getAmmount());
        result.setDate(transactionEntity.getDate());
        result.setId(transactionEntity.getId());

        String username = usersWithIdCache.get(transactionEntity.getUser_id());
        if (username == null) {
            username=userDao.getName(transactionEntity.getUser_id());
            usersWithIdCache.put(transactionEntity.getUser_id(),username);
        }
        result.setUsername(username);

        String category = categoriesWithIdCache.get(transactionEntity.getCategory_id());
        if (category == null){
            category=categoryDao.getName(transactionEntity.getCategory_id(),transactionEntity.getUser_id());
            categoriesWithIdCache.put(transactionEntity.getCategory_id(),category);
        }
        result.setCategory(category);

        return result;
    }

    public Transaction restInputToService(TransactionInput transactionInput,String username) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmmount(transactionInput.getAmount());
        transaction.setExpense(transactionInput.getIsExpense());
        transaction.setName(transactionInput.getName());
        transaction.setCategory(transactionInput.getCategory());
        transaction.setUsername(username);

        transaction.setDate(stringToDate(transactionInput.getDate()));
        transaction.setPaymentType(stringToPayment(transactionInput.getPaymentType()));
        transaction.setCurrency(stringToCurrency(transactionInput.getCurrency()));
        return transaction;
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

    public TransactionEntity serviceToDao(Transaction transaction, int user_id, int category_id) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setId(transaction.getId());
        transactionEntity.setAmmount(transaction.getAmmount());
        transactionEntity.setCategory_id(category_id);
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setExpense(transaction.isExpense());
        transactionEntity.setName(transaction.getName());
        transactionEntity.setPaymentType(transaction.getPaymentType());
        transactionEntity.setUser_id(user_id);
        return transactionEntity;
    }
}
