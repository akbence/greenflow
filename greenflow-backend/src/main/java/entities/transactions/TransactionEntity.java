package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = CategoryEntity.tableName)
public class TransactionEntity {

    public static final String tableName= "Transaction";

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
    private String unit;

    @Column
    boolean isCard;

    @Column
    private LocalDateTime date;



    public TransactionEntity(int user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    public TransactionEntity(){};

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
