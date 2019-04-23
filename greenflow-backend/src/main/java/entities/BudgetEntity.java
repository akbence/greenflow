package entities;

import javax.persistence.*;

import java.time.LocalDate;

@Entity
@NamedQueries({@NamedQuery(name = "Budget.getIdByCurrencyPaymentTypePeriodUserId", query = "select c from BudgetEntity c where c.user_id = :user_id and c.currency = :currency " +
        "and c.paymentType = :paymentType and  function('month',c.period) = :month and function('year',c.period) = :year" )})
@Table(name = BudgetEntity.tableName)
public class BudgetEntity {

    public static final String tableName= "Budget";
    public static final String QUERY_CATEGORY_GET_BY_CURR_PTYPE_PERIOD_USERID= "Budget.getIdByCurrencyPaymentTypePeriodUserId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int user_id;

    @Column
    private LocalDate period;

    @Column
    private int limitation;

    @Column
    private String currency;

    @Column
    private String paymentType;

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

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public int getLimitation() {
        return limitation;
    }

    public void setLimitation(int limitation) {
        this.limitation = limitation;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
