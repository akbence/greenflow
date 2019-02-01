package service.authentication;

import dao.UserDao;
import inputs.UserInput;

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

    public void registerUser(UserInput userInput) throws Exception {

        User newUser = new User();
        LocalDateTime creationDate = LocalDateTime.now();
        newUser.setRegistration_date(creationDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        newUser.setPasswordHash(passwordHandler.createPasswordHash(userInput.getPassword(), creationDate.format(formatter)));
        newUser.setUsername(userInput.getUsername());
        newUser.setRegistration_date(creationDate);
        userDao.registerUser(newUser);
    }
}
