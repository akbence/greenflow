package dao;

import entities.BudgetEntity;
import service.budget.Budget;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Model
public class BudgetDao {

    @Inject
    EntityManager em;

    @Inject
    UserDao userDao;

    @Transactional
    public void addBudget(Budget budget) {
        BudgetEntity budgetEntity= new BudgetEntity();
        budgetEntity.setLimitation(budget.getLimit());
        budgetEntity.setPeriod(budget.getPeriod());
        budgetEntity.setUser_id(userDao.getId(budget.getUsername()));
        budgetEntity.setCurrency(budget.getCurrency().toString());
        budgetEntity.setPaymentType(budget.getPaymentType().toString());

        try {
            BudgetEntity result= em.createNamedQuery(BudgetEntity.QUERY_CATEGORY_GET_BY_CURR_PTYPE_PERIOD_USERID, BudgetEntity.class)
                    .setParameter("currency",budgetEntity.getCurrency())
                    .setParameter("paymentType",budgetEntity.getPaymentType())
                    .setParameter("period",budgetEntity.getPeriod())
                    .setParameter("user_id",budgetEntity.getUser_id())
                    .getSingleResult();
            if(result==null){
                em.persist(budgetEntity);
            }else throw new Exception("cant add, actual_monthly exists");
        }
        catch (Exception e){
            System.out.println("add budget problem");
        }
    }
}
