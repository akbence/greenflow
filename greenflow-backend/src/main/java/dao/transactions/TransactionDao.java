package dao.transactions;

import dao.UserDao;
import entities.transactions.CategoryEntity;
import entities.transactions.TransactionEntity;
import service.transaction.Category;
import service.transaction.Transaction;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class TransactionDao {


    @Inject
    private EntityManager em;

    @Inject
    UserDao userDao;

    @Inject
    private CategoryDao categoryDao;

    @Transactional
    public void post(Transaction transaction) {
        TransactionEntity transactionEntity= new TransactionEntity();
        transactionEntity.setAmmount(transaction.getAmmount());
        transactionEntity.setCurrency(transaction.getCurrency());
        transactionEntity.setDate(transaction.getDate());
        transactionEntity.setExpense(transaction.isExpense());
        transactionEntity.setName(transaction.getName());
        transactionEntity.setPaymentType(transaction.getPaymentType());

        int userId=userDao.getId(transaction.getName());

        transactionEntity.setCategory_id(categoryDao.getID(transaction.getCategory(),userId));
        transactionEntity.setUser_id(userId);

        try {
            em.persist(transactionEntity);
        }
        catch (Exception e){
            System.out.println("add category problem");
        }

    }
}
