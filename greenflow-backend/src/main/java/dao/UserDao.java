package dao;

import service.authentication.User;
import entities.UserEntity;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Model
public class UserDao {

    @Inject
    private EntityManager em;

    @Transactional
    public void registerUser(User user){
        UserEntity userEntity=new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPasswordHash(user.getPasswordHash());
        userEntity.setRegistrationDate(user.getRegistration_date());

        try {
            em.persist(userEntity);
        }
        catch (Exception e){

            e.printStackTrace();

        }
    }

}
