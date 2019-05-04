package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NamedQueries({ @NamedQuery(name = "User.getPasswordHash", query = "select u.passwordHash from UserEntity u where u.username = :username"),
         @NamedQuery(name = "User.getCreationDate", query = "select u.registrationDate from UserEntity u where u.username = :username"),
        @NamedQuery(name = "User.getID", query = "select u.id from UserEntity u where u.username = :username"),
        @NamedQuery(name = "User.getUsername", query = "select u.username from UserEntity u where u.id = :id"),
        @NamedQuery(name = "User.getEmailByUsername", query = "select u.email from UserEntity u where u.username = :username"),
        @NamedQuery(name = "User.getEmailById", query = "select u.email from UserEntity u where u.id = :id"),
        @NamedQuery(name = "User.getByUsername", query = "select u from UserEntity u where u.username = :username")})
@Table(name = UserEntity.tableName)
public class UserEntity {

    public static final String QUERY_USER_GET_PASSWORDHASH_BY_USERNAME = "User.getPasswordHash";
    public static final String QUERY_USER_GET_CREATIONDATE_BY_USERNAME = "User.getCreationDate";
    public static final String QUERY_USER_GET_ID_BY_USERNAME = "User.getID";
    public static final String QUERY_USER_GET_USERNAME_BY_ID = "User.getUsername";
    public static final String tableName= "User";
    public static final String QUERY_USER_GET_BY_USERNAME = "User.getByUsername";
    public static final String QUERY_USER_GET_EMAIL_BY_USERNAME = "User.getEmailByUsername";
    public static final String QUERY_USER_GET_EMAIL_BY_ID = "User.getEmailById";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String username;

    @Column
    private String passwordHash;

    @Column
    private LocalDateTime registrationDate;

    @Column
    private String email;

    public UserEntity(String username, String passwordHash, LocalDateTime timestamp) {
        this.username = username;
        this.passwordHash= passwordHash;
        this.registrationDate = timestamp;
    };

    public UserEntity() {
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
