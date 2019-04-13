package service.transaction;

public class Category {

    private int id;
    private String name;
    private String username;

    public Category(String name, String username) {
        this.name = name;
        this.username = username;
    }
    public Category(){};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
