package service.authentication;

import dao.UserDao;
import inputs.UserAuthInput;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Model
public class AuthService {

    @Inject
    UserDao userDao;

    @Inject
    PasswordHandler passwordHandler;

    public void registerUser(UserAuthInput userAuthInput) throws Exception {

        User newUser = new User();
        LocalDateTime creationDate = LocalDateTime.now();
        newUser.setRegistration_date(creationDate);
        newUser.setUsername(userAuthInput.getUsername());
        newUser.setPasswordHash(passwordHandler.createPasswordHash(userAuthInput.getPassword(), creationDate));
        userDao.registerUser(newUser);
    }

    public boolean loginUser(UserAuthInput userAuthInput) throws Exception {
        User loginUser = new User();
        loginUser.setUsername(userAuthInput.getUsername());
        String passwordHash = userDao.getPasswordHash(loginUser);

        String authHash = passwordHandler.createPasswordHash(userAuthInput.getPassword(), userDao.getCreationDate(loginUser));

        if (passwordHash.isEmpty() || !passwordHash.equals(authHash)) {
            throw new Exception("Password incorrect");
        } else {
            return true;
        }
    }
}
