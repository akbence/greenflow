package service.authentication;

import javax.enterprise.inject.Model;
import java.time.LocalDateTime;

@Model
public class User {
    private String username;
    private String passwordHash;
    private LocalDateTime registration_date;
    private String token;
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String password) { this.passwordHash = password; }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getRegistration_date() {
        return registration_date;
    }

    public void setRegistration_date(LocalDateTime registration_date) {
        this.registration_date = registration_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}