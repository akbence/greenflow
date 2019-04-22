package entities.transactions;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = BudgetEntity.tableName)
public class BudgetEntity {

    public static final String tableName= "Budget";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private int user_id;

    @Column
    private LocalDate period;

    @Column int limit;

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

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
