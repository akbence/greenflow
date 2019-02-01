package dao;

import entities.User;
import inputs.UserInput;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Model
public class UserDao {

    @Inject
    EntityManager em;

    public void registerUser(UserInput userInput){
        User user=new User();
        user

        try {
            em.persist();
        }
        catch (Exception e){
            System.out.println("adduser problem");
        }
    }

}
