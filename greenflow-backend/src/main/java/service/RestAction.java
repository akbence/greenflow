package service;

import service.authentication.LoggedIn;
import service.authentication.User;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class RestAction {

    @Inject
    @LoggedIn
    User currentUser;

    public String quickTest(){
        return currentUser.getUsername();
    }
}
