package dao;

import entities.UserEntity;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Model
public class GreenflowDao {

    @Inject
    private EntityManager em;


    @Transactional
    public void addUser(){
        try {
            //em.persist(new User("testUser"));
//            em.persist();
        }
        catch (Exception e){
            System.out.println("adduser problem");
        }
    }
}