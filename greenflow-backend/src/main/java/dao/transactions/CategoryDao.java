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

    public int getID(String category,int userID) {

        System.out.println(category + " " + userID);

        int result = -1;
        try {
            result = em.createNamedQuery(CategoryEntity.QUERY_CATEGORY_GET_ID_BY_NAME, Integer.class)
                    .setParameter("name",category)
                    .setParameter("user_id",userID)
                    .getSingleResult();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        return result;
    }
}
