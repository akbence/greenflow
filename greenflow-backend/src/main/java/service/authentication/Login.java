package service.authentication;

import inputs.UserInput;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@SessionScoped
@Named
public class Login implements Serializable {

    @Inject PasswordHandler passwordHandler;

    private User user;

    public void login(UserInput userInput) {
        User user=new User();
        user.setUsername(userInput.getUsername());
        //user.setPasswordHash(passwordHandler);



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