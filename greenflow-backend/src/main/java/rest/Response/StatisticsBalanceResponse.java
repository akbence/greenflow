package rest.Response;

import enums.Currency;
import enums.PaymentType;

public class StatisticsBalanceResponse {
    private Currency currency;
    private int currentTotalBalance;
    private int currentCashBalance;
    private int currentCardBalance;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getCurrentTotalBalance() {
        return currentTotalBalance;
    }

    public void setCurrentTotalBalance(int currentTotalBalance) {
        this.currentTotalBalance = currentTotalBalance;
    }

    public int getCurrentCashBalance() {
        return currentCashBalance;
    }

    public void setCurrentCashBalance(int currentCashBalance) {
        this.currentCashBalance = currentCashBalance;
    }

    public int getCurrentCardBalance() {
        return currentCardBalance;
    }

    public void setCurrentCardBalance(int currentCardBalance) {
        this.currentCardBalance = currentCardBalance;
    }
}
