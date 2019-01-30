package action;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class RestAction {

    @Inject
    @LoggedIn User currentUser;

    public String quickTest(){
        return currentUser.getUsername();
    }
}
