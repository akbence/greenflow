package dao.transactions;

import converters.TransactionConverter;
import dao.UserDao;
import entities.UserEntity;
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

            int user_id= userDao.getId(username);
            entityList= (ArrayList<TransactionEntity>) em.createNamedQuery(TransactionEntity.QUERY_TRANSACTION_GETALL_BY_USERNAME,TransactionEntity.class).setParameter("user_id",user_id).getResultList();


        return transactionConverter.daoToServiceList(entityList);
    }

    @Transactional
    public void delete(int id, String username) throws Exception{
        TransactionEntity transactionEntity = em.find(TransactionEntity.class,id);
        int userid = userDao.getId(username);
        if(userid==transactionEntity.getUser_id()){
            em.remove(transactionEntity);
        }
        else throw new Exception("unauthorized access");
    }
}
