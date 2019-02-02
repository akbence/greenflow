package dao;

import service.authentication.User;
import entities.UserEntity;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Model
public class UserDao {

    @Inject
    private EntityManager em;

    @Transactional
    public void registerUser(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setRegistrationDate(user.getRegistration_date());

        try {
            em.persist(userEntity);
        } catch (Exception e) {

            e.printStackTrace();

        }
    }

    @Transactional
    public String getPasswordHash(User loginUser) {
        String result = null;
        try {
            UserEntity user = em.find(UserEntity.class, loginUser.getUsername());
            result = user.getPasswordHash();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public LocalDateTime getCreationDate(User loginUser) {
        LocalDateTime result = null;
        try {
            UserEntity user = em.find(UserEntity.class, loginUser.getUsername());
            result = user.getRegistrationDate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
