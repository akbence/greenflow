package service.authentication;

import dao.UserDao;
import rest.Input.UserAuthInput;
import rest.Response.LoginResponse;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.LocalDateTime;

@SessionScoped
public class AuthService implements Serializable {

    @Inject
    UserDao userDao;

    @Inject
    PasswordHandler passwordHandler;

    @Inject
    LoggedInService loggedInService;

    public void registerUser(UserAuthInput userAuthInput) throws Exception {

        System.out.println("problem at Service");

        User newUser = new User();
        LocalDateTime creationDate = LocalDateTime.now();
        newUser.setRegistration_date(creationDate);
        newUser.setUsername(userAuthInput.getUsername());
        newUser.setPasswordHash(passwordHandler.createPasswordHash(userAuthInput.getPassword(), creationDate));
        userDao.registerUser(newUser);
    }

    public LoginResponse loginUser(UserAuthInput userAuthInput) throws Exception {
        User loginUser = new User();
        loginUser.setUsername(userAuthInput.getUsername());
        String passwordHash = userDao.getPasswordHash(loginUser);
        loginUser.setRegistration_date(userDao.getCreationDate(loginUser));
        String authHash = passwordHandler.createPasswordHash(userAuthInput.getPassword(), loginUser.getRegistration_date());

        if (passwordHash.isEmpty() || !passwordHash.equals(authHash)) {
            throw new Exception("Password incorrect");
        } else {
            //TODO: Need to implement a good auth token here. E.g. JWT_AUTH_TOKEN
            loginUser.setToken(loginUser.getUsername()+"_"+System.currentTimeMillis());
            loggedInService.login(loginUser);
            LoginResponse response = new LoginResponse();
            response.setToken(loginUser.getToken());
            response.setUsername(loginUser.getUsername());
            response.setRegistrationDate(loginUser.getRegistration_date().toLocalDate());
            return  response;
        }
    }
}
