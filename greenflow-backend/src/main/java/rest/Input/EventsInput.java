package rest.Input;

public class EventsInput {
    private boolean monthlyreport;
    private boolean warningreport;

    public boolean isMonthlyreport() {
        return monthlyreport;
    }

    public void setMonthlyreport(boolean monthlyreport) {
        this.monthlyreport = monthlyreport;
    }

    public boolean isWarningreport() {
        return warningreport;
    }

    public void setWarningreport(boolean warningreport) {
        this.warningreport = warningreport;
    }
}
