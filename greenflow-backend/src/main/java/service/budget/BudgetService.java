package service.budget;

import dao.BudgetDao;
import rest.Input.BudgetInput;
import service.authentication.LoggedIn;
import service.authentication.LoggedInService;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class BudgetService {

    @Inject
    LoggedInService loggedInService;

    @Inject
    BudgetDao budgetDao;

    public void addBudget(BudgetInput budgetInput) {
        Budget budget = new Budget();
        budget.setUsername(loggedInService.getCurrentUserName());
        budget.setLimit(budgetInput.getLimit());
        budget.setPeriod(budgetInput.getPeriod());
        budgetDao.addBudget(budget);
    }
}
