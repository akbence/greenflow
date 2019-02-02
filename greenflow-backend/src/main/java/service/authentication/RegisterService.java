package service.authentication;

import dao.UserDao;
import inputs.UserAuthInput;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Model
public class RegisterService {

    @Inject
    UserDao userDao;

    @Inject
    PasswordHandler passwordHandler;

    public void registerUser(UserAuthInput userAuthInput) throws Exception {

        User newUser = new User();
        LocalDateTime creationDate = LocalDateTime.now();
        newUser.setRegistration_date(creationDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        newUser.setPasswordHash(passwordHandler.createPasswordHash(userAuthInput.getPassword(), creationDate.format(formatter)));
        newUser.setUsername(userAuthInput.getUsername());
        newUser.setRegistration_date(creationDate);
        userDao.registerUser(newUser);
    }

    public void loginUser(UserAuthInput userAuthInput) throws Exception {
        User loginUser = new User();
        loginUser.setUsername(userAuthInput.getUsername());
        String passwordHash = userDao.getPasswordHash(loginUser);
        LocalDateTime creationDate = userDao.getCreationDate(loginUser);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String authHash=passwordHandler.createPasswordHash(userAuthInput.getPassword(), creationDate.format(formatter));

        if (passwordHash.isEmpty() || !passwordHash.equals(authHash)) {
            throw new Exception("Password incorrect");
        }
        else{
            //TODO Logged in set here
        }


    }
}
