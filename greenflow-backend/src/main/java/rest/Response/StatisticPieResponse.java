package rest.Response;

import enums.Currency;
import enums.PaymentType;

import java.util.List;

public class StatisticPieResponse {

    private PaymentType paymentType;
    private Currency currency;
    private List<String> labels;
    private List<Integer> data;
    private boolean isRelevant;

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

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public boolean isRelevant() {
        return isRelevant;
    }

    public void setRelevant(boolean relevant) {
        isRelevant = relevant;
    }
}
