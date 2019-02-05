package service.authentication;

import dao.UserDao;
import inputs.UserAuthInput;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class LoggedInService implements Serializable {

    @Inject PasswordHandler passwordHandler;

    private User user;


    public void login(User  loggingInUser) {
        user=loggingInUser;
    }

    public void logout() {
        user = null;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    @Produces
    @LoggedIn
    User getCurrentUser() {
        return user;
    }

}