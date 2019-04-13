package converters;

import dao.UserDao;
import dao.transactions.CategoryDao;
import entities.transactions.TransactionEntity;
import service.transaction.Transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
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
}
