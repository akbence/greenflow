package rest.Input;

import enums.Currency;
import enums.PaymentType;

import java.time.LocalDate;

public class BudgetInput {

    private PaymentType paymentType;
    private Currency currency;
    private int limit;

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

}