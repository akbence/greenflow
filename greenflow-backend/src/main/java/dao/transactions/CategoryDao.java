package dao.transactions;

import service.authentication.User;
import entities.UserEntity;

import javax.ejb.Local;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Model
public class CategoryDao {

    @Inject
    private EntityManager em;

    @Transactional
    public void getCategory(User user) {
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

    public LocalDateTime getCreationDate(User loginUser) {
        LocalDateTime result = null;
        try {
            result = em.createNamedQuery(UserEntity.QUERY_USER_GET_CREATIONDATE_BY_USERNAME, LocalDateTime.class).setParameter("username",loginUser.getUsername()).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
