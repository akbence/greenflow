package entities.transactions;

import enums.Currency;
import enums.PaymentType;
import service.transaction.Transaction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = TransactionEntity.tableName)
public class TransactionEntity {

    public static final String tableName = "Transaction";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int user_id;

    @Column
    private int category_id;

    @Column
    private String name;

    @Column
    private boolean isExpense;

    @Column
    private int ammount;


    @Column
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column
    private LocalDate date;

    public TransactionEntity(int user_id, int category_id, String name, boolean isExpense, int ammount, PaymentType paymentType, Currency currency, LocalDate date) {
        this.user_id = user_id;
        this.category_id = category_id;
        this.name = name;
        this.isExpense = isExpense;
        this.ammount = ammount;
        this.paymentType = paymentType;
        this.currency = currency;
        this.date = date;
    }

    public TransactionEntity() {
    }

    public static String getTableName() {
        return tableName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
