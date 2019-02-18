package entities.transactions;

import javax.persistence.*;

@Entity
@Table(name = CategoryEntity.tableName)
public class CategoryEntity {

    public static final String tableName= "Category";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private int user_id;

    @Column
    private String name;

    public CategoryEntity(int user_id, String name) {
        this.user_id = user_id;
        this.name = name;
    }

    public CategoryEntity(){};

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
