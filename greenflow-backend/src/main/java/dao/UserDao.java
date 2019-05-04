package dao;

import service.authentication.User;
import entities.UserEntity;

import javax.ejb.Local;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Named
@RequestScoped
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
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_PASSWORDHASH_BY_USERNAME,String.class).setParameter("username",loginUser.getUsername()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public LocalDateTime getCreationDate(User loginUser) {
        LocalDateTime result = null;
        try {
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_CREATIONDATE_BY_USERNAME, LocalDateTime.class).setParameter("username",loginUser.getUsername()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public int getId(String name){
        int result = -1;
        try {
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_ID_BY_USERNAME, Integer.class).setParameter("username",name).getSingleResult();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        System.out.println(name+" " + result);
        return result;
    }

    public String getName(int user_id) {
        String result = null;
        try {
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_USERNAME_BY_ID, String.class).setParameter("id",user_id).getSingleResult();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public void addEmail(String username, String email) {
        try {
            UserEntity result = em.createNamedQuery(UserEntity.QUERY_USER_GET_BY_USERNAME, UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            result.setEmail(email);
            em.merge(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEmail(User loginUser) {
        String username = loginUser.getUsername();
        return getEmail(username);
    }

    public String getEmail(String username) {
        String result = null;
        try {
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_EMAIL_BY_USERNAME, String.class).setParameter("username",username).getSingleResult();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public void deleteEmail(String username) {
        try {
            UserEntity result = em.createNamedQuery(UserEntity.QUERY_USER_GET_BY_USERNAME, UserEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            result.setEmail(null);
            em.merge(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
