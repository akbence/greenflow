package service.transaction;

import service.authentication.LoggedIn;
import service.authentication.User;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

@Model
public class TransactionService {

    private User user;



    public boolean isLoggedIn() {
        return user != null;
    }

    @Produces
    @LoggedIn
    User getCurrentUser() {
        return user;
    }

}
