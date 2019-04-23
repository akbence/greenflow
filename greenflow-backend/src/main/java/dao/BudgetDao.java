package dao;

import entities.BudgetEntity;
import service.budget.Budget;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

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
            List<BudgetEntity> result = em.createNamedQuery(BudgetEntity.QUERY_CATEGORY_GET_BY_CURR_PTYPE_PERIOD_USERID, BudgetEntity.class)
                    .setParameter("currency", budgetEntity.getCurrency())
                    .setParameter("paymentType", budgetEntity.getPaymentType())
                    .setParameter("month", budgetEntity.getPeriod().getMonthValue())
                    .setParameter("year", budgetEntity.getPeriod().getYear())
                    .setParameter("user_id", budgetEntity.getUser_id())
                    .getResultList();
            if(result.isEmpty()){
                em.persist(budgetEntity);
            }else throw new Exception("cant add, actual_monthly exists");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("add budget problem");
        }
    }
}
