package rest.Response;

import java.util.List;

public class StatisticBarResponse {

    private List<String> labels;
    private List<Integer> expenseData;
    private List<Integer> incomeData;

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Integer> getExpenseData() {
        return expenseData;
    }

    public void setExpenseData(List<Integer> expenseData) {
        this.expenseData = expenseData;
    }

    public List<Integer> getIncomeData() {
        return incomeData;
    }

    public void setIncomeData(List<Integer> incomeData) {
        this.incomeData = incomeData;
    }
}
