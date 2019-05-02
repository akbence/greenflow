package dao;

import converters.BudgetConverter;
import entities.BudgetEntity;
import service.budget.Budget;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Model
public class BudgetDao {

    @Inject
    private EntityManager em;

    @Inject
    private UserDao userDao;

    @Inject
    private BudgetConverter budgetConverter;

    @Transactional
    public void addBudget(Budget budget) {
        BudgetEntity budgetEntity= new BudgetEntity();
        budgetEntity.setLimitation(budget.getLimit());
        budgetEntity.setPeriod(budget.getPeriod());
        budgetEntity.setUser_id(userDao.getId(budget.getUsername()));
        budgetEntity.setCurrency(budget.getCurrency().toString());
        budgetEntity.setPaymentType(budget.getPaymentType().toString());

        try {
            List<BudgetEntity> result = em.createNamedQuery(BudgetEntity.QUERY_BUDGET_GET_BY_CURR_PTYPE_PERIOD_USERID, BudgetEntity.class)
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

    public List<Budget> getAllBudget(String currentUserName) {
        int userId= userDao.getId(currentUserName);
        try{
            List<BudgetEntity> budgetEntities=em.createNamedQuery(BudgetEntity.QUERY_BUDGET_GET_ALL_BY_ID,BudgetEntity.class)
                    .setParameter("user_id", userId)
                    .getResultList();
            return budgetConverter.daoToServiceList(budgetEntities);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("add budget problem");
            throw e;
        }
    }

    @Transactional
    public void modifyBudgetLimit(int id, int limit, String username) {
        int userId= userDao.getId(username);
        try{
            BudgetEntity budgetEntity =em.createNamedQuery(BudgetEntity.QUERY_BUDGET_BY_ID_USERID,BudgetEntity.class)
                    .setParameter("user_id", userId)
                    .setParameter("id",id)
                    .getSingleResult();
            budgetEntity.setLimitation(limit);
            em.merge(budgetEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("modify budget problem, unathorized access");
            throw e;
        }
    }

    @Transactional
    public void deleteBudget(int id, String username) {
        int userId= userDao.getId(username);
        try{
            BudgetEntity budgetEntity =em.createNamedQuery(BudgetEntity.QUERY_BUDGET_BY_ID_USERID,BudgetEntity.class)
                    .setParameter("user_id", userId)
                    .setParameter("id",id)
                    .getSingleResult();
            em.remove(budgetEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("remove budget problem, unathorized access");
            throw e;
        }
    }

    public List<Budget> getMonthlyBudget(String currentUserName, LocalDate period) {
        int userId= userDao.getId(currentUserName);
        try{
            List<BudgetEntity> budgetEntities=em.createNamedQuery(BudgetEntity.QUERY_BUDGET_GET_ALL_BY_PERIOD,BudgetEntity.class)
                    .setParameter("user_id", userId)
                    .setParameter("month", period.getMonthValue())
                    .setParameter("year", period.getYear())
                    .getResultList();
            return budgetConverter.daoToServiceList(budgetEntities);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("add budget problem");
            throw e;
        }
    }


    @Transactional
    public void modifyBudgetWarning(int id, int limit, String username) {
        int userId= userDao.getId(username);
        try{
            BudgetEntity budgetEntity =em.createNamedQuery(BudgetEntity.QUERY_BUDGET_BY_ID_USERID,BudgetEntity.class)
                    .setParameter("user_id", userId)
                    .setParameter("id",id)
                    .getSingleResult();
            budgetEntity.setWarning(limit);
            em.merge(budgetEntity);
        }catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("modify budget problem, unathorized access");
            throw e;
        }
    }
}
