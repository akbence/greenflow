package rest.Input;

import java.time.LocalDate;

public class BudgetInput {

    private int limit;
    private LocalDate period;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }
}
