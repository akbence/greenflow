package dao.transactions;

import converters.TransactionConverter;
import dao.UserDao;
import entities.UserEntity;
import entities.transactions.CategoryEntity;
import entities.transactions.TransactionEntity;
import enums.Currency;
import enums.PaymentType;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {


    @Inject
    private EntityManager em;

    @Inject
    UserDao userDao;

    @Inject
    private CategoryDao categoryDao;

    @Inject
    TransactionConverter transactionConverter;

    @Transactional
    public void post(Transaction transaction) {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setAmmount(transaction.getAmmount());
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setExpense(transaction.isExpense());
        transactionEntity.setName(transaction.getName());
        transactionEntity.setPaymentType(transaction.getPaymentType());

        int userId = userDao.getId(transaction.getUsername());

        transactionEntity.setCategory_id(categoryDao.getID(transaction.getCategory(), userId));
        transactionEntity.setUser_id(userId);

        try {
            em.persist(transactionEntity);
        } catch (Exception e) {
            System.out.println("add category problem");
        }

    }

    @Transactional
    public ArrayList<Transaction> getEntireTransactionHistory(String username) throws Exception {
        ArrayList<TransactionEntity> entityList = new ArrayList<>();
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETALL_BY_USERNAME, TransactionEntity.class).setParameter("user_id", user_id).getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    @Transactional
    public void delete(int id, String username) throws Exception {
        TransactionEntity transactionEntity = em.find(TransactionEntity.class, id);
        int userid = userDao.getId(username);
        if (userid == transactionEntity.getUser_id()) {
            em.remove(transactionEntity);
        } else throw new Exception("unauthorized access");
    }

    public List<Transaction> getMonthlyTransactions(String username, int year, int month) {
        ArrayList<TransactionEntity> entityList;
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETMONTHLY_BY_USERNAME_EXPENSE_AND_INCOME, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("year", year)
                .setParameter("month", month)
                .getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    public ArrayList<Transaction> getMonthlyTransactions(String username, int year, int month, boolean isExpense) {
        ArrayList<TransactionEntity> entityList;
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETMONTHLY_BY_USERNAME, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("isExp", isExpense)
                .getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    public ArrayList<Transaction> getMonthlyTransactions(String username, int year, int month, boolean isExpense, Currency currency) {
        ArrayList<TransactionEntity> entityList;
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETMONTHLY_BY_USERNAME_CURRENCY, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("isExp", isExpense)
                .setParameter("currency",currency)
                .getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    public ArrayList<Transaction>  getMonthlyTransactions(String username, int year, int month, boolean isExpense, Currency currency, PaymentType paymentType) {
        ArrayList<TransactionEntity> entityList;
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETMONTHLY_BY_USERNAME_CURRENCY_PAYMENTTYPE, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("year", year)
                .setParameter("month", month)
                .setParameter("isExp", isExpense)
                .setParameter("currency",currency)
                .setParameter("paymentType",paymentType)
                .getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    @Transactional
    public void modify(Transaction transaction, int category_id) throws Exception {
        TransactionEntity transactionEntity = em.find(TransactionEntity.class, transaction.getId());
        int userid = userDao.getId(transaction.getUsername());
        if (userid == transactionEntity.getUser_id()) {
            TransactionEntity replace = transactionConverter.serviceToDao(transaction, transactionEntity.getUser_id(), category_id);
            em.merge(replace);
        } else throw new Exception("unauthorized access");
    }

    public ArrayList<Transaction> getGivenPeriodTransactionHistory(String username, LocalDate fromDate, LocalDate toDate) {
        ArrayList<TransactionEntity> entityList = new ArrayList<>();
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GET_GIVEN_PERIOD_BY_USERNAME, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("fromDate", fromDate)
                .setParameter("toDate", toDate)
                .getResultList();
        return transactionConverter.daoToServiceList(entityList);
    }

    @Transactional
    public void deleteAllWithCategory(int category_id,String username) {
        ArrayList<TransactionEntity> entityList = new ArrayList<>();
        int user_id = userDao.getId(username);
        entityList = (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GET_ALL_BY_CATEGORY, TransactionEntity.class)
                .setParameter("user_id", user_id)
                .setParameter("category_id", category_id)
                .getResultList();
        entityList.forEach(entity ->
                em.remove(entity));
    }
}