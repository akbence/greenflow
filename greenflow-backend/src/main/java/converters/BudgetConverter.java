package converters;

import entities.BudgetEntity;
import enums.Currency;
import enums.PaymentType;
import rest.Response.BudgetResponse;
import service.budget.Budget;

import javax.enterprise.inject.Model;
import java.util.ArrayList;
import java.util.List;

@Model
public class BudgetConverter {


    public List<BudgetResponse> serviceToRestList(List<Budget> budgetList){
        List<BudgetResponse> ret=new ArrayList<>();
        for (Budget budget : budgetList) {
            ret.add(serviceToRest(budget));
        }
        return ret;
    }

    public BudgetResponse serviceToRest(Budget budget){

        BudgetResponse temp = new BudgetResponse();
        temp.setCurrency(budget.getCurrency());
        temp.setLimit(budget.getLimit());
        temp.setPaymentType(budget.getPaymentType());
        temp.setId(budget.getId());
        temp.setWarning(budget.getWarning());
        return temp;
    }

    public List<Budget> daoToServiceList(List<BudgetEntity> budgetEntities) {
        List<Budget> ret=new ArrayList<>();
        for (BudgetEntity budget : budgetEntities) {
            ret.add(daoToService(budget));
        }
        return ret;
    }
    public Budget daoToService(BudgetEntity budget){
        Budget temp = new Budget();
        temp.setCurrency(stringToCurrency(budget.getCurrency()));
        temp.setLimit(budget.getLimitation());
        temp.setPaymentType(stringToPaymenType(budget.getPaymentType()));
        temp.setId(budget.getId());
        temp.setWarning(budget.getWarning());
        return temp;
    }

    private Currency stringToCurrency(String currency){
        return Currency.valueOf(currency);
    }
    private PaymentType stringToPaymenType(String paymentType){
        return PaymentType.valueOf(paymentType);
    }

}
