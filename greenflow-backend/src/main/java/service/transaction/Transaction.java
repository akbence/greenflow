package service.transaction;

import enums.Currency;
import enums.PaymentType;

import java.time.LocalDateTime;


public class Transaction {

    private String username;

    private String category;

    private String name;

    private boolean isExpense;

    private int ammount;

    private PaymentType paymentType;

    private Currency currency;

    private LocalDateTime date;

    public Transaction(String username, String category, String name, boolean isExpense, int ammount, PaymentType paymentType, Currency currency, LocalDateTime date) {
        this.username = username;
        this.category = category;
        this.name = name;
        this.isExpense = isExpense;
        this.ammount = ammount;
        this.paymentType = paymentType;
        this.currency = currency;
        this.date = date;
    }

    public Transaction() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExpense() {
        return isExpense;
    }

    public void setExpense(boolean expense) {
        isExpense = expense;
    }

    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}

