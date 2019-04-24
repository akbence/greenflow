package service.budget;

import converters.BudgetConverter;
import dao.BudgetDao;
import entities.BudgetEntity;
import rest.Input.BudgetInput;
import rest.Response.BudgetResponse;
import service.authentication.LoggedIn;
import service.authentication.LoggedInService;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Model
public class BudgetService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private BudgetDao budgetDao;

    @Inject
    private BudgetConverter budgetConverter;

    public void addBudget(BudgetInput budgetInput) {
        Budget budget = new Budget();
        budget.setUsername(loggedInService.getCurrentUserName());
        budget.setLimit(budgetInput.getLimit());
        budget.setPeriod(LocalDate.now());
        budget.setCurrency(budgetInput.getCurrency());
        budget.setPaymentType(budgetInput.getPaymentType());
        budgetDao.addBudget(budget);
    }

    public List<BudgetResponse> getAllBudget() {
        List<Budget> budgetList = budgetDao.getAllBudget(loggedInService.getCurrentUserName());
        return budgetConverter.serviceToRestList(budgetList);
    }

    public void modifyBudget(int id,int limit) {
        String username = loggedInService.getCurrentUserName();
        budgetDao.modifyBudget(id,limit,username);
    }
}
