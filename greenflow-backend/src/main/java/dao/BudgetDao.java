package dao;

import entities.transactions.BudgetEntity;
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
        budgetEntity.setLimit(budget.getLimit());
        budgetEntity.setPeriod(budget.getPeriod());
        budgetEntity.setUser_id(userDao.getId(budget.getUsername()));
        try {
            em.persist(budgetEntity);
        }
        catch (Exception e){
            System.out.println("add budget problem");
        }
    }
}
