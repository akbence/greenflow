package dao.transactions;

import dao.UserDao;
import entities.transactions.CategoryEntity;
import service.transaction.Category;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@Model
public class CategoryDao {

    @Inject
    private EntityManager em;

    @Inject
    UserDao userDao;

    @Transactional
    public void post(Category category) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setUser_id(userDao.getId(category.getUsername()));
        try {

            em.persist(categoryEntity);
        }
        catch (Exception e){
            System.out.println("add category problem");
        }

    }
}
