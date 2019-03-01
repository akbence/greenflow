package dao.transactions;

import converters.TransactionConverter;
import dao.UserDao;
import entities.transactions.CategoryEntity;
import entities.transactions.TransactionEntity;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;

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
        TransactionEntity transactionEntity= new TransactionEntity();
        transactionEntity.setAmmount(transaction.getAmmount());
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setExpense(transaction.isExpense());
        transactionEntity.setName(transaction.getName());
        transactionEntity.setPaymentType(transaction.getPaymentType());

        int userId=userDao.getId(transaction.getUsername());

        transactionEntity.setCategory_id(categoryDao.getID(transaction.getCategory(),userId));
        transactionEntity.setUser_id(userId);

        try {
            em.persist(transactionEntity);
        }
        catch (Exception e){
            System.out.println("add category problem");
        }

    }

    @Transactional
    public ArrayList<Transaction> getTransactions(String username) throws Exception{
        ArrayList <TransactionEntity> entityList= new ArrayList<>();
        try {
            entityList= (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETALL_BY_USERNAME,TransactionEntity.class).setParameter("username",username).getResultList();

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return transactionConverter.daoToServiceList(entityList);
    }
}
