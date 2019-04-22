package dao.transactions;

import converters.CategoryConverter;
import dao.UserDao;
import entities.transactions.CategoryEntity;
import service.transaction.Category;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;


@Model
public class CategoryDao {

    @Inject
    private EntityManager em;

    @Inject
    UserDao userDao;

    @Inject
    CategoryConverter categoryConverter;

    @Transactional
    public void addCategory(Category category) {
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

    public String getName(int category_id, int user_id) {
        String result = null;
        try {
            result = em.createNamedQuery(CategoryEntity.QUERY_CATEGORY_GET_NAME_BY_IDS, String.class)
                    .setParameter("category_id",category_id)
                    .setParameter("user_id",user_id)
                    .getSingleResult();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        return result;
    }

    public ArrayList<Category> getAllById(int user_id) {
        ArrayList<CategoryEntity> result = null;
        try {
            result = (ArrayList<CategoryEntity>) em.createNamedQuery(CategoryEntity.QUERY_CATEGORY_GET_CATEGORIES_BY_IDS, CategoryEntity.class)
                    .setParameter("user_id",user_id)
                    .getResultList();
        }
        catch (Exception e ){
            e.printStackTrace();
        }

        return categoryConverter.daoToServiceList(result);
    }

    @Transactional
    public void delete(int userId, int id) {
        CategoryEntity result = null;
        try {

            CategoryEntity ce= em.find(CategoryEntity.class,id);
            if(userId == ce.getUser_id()){
                em.remove(ce);
            }
            else throw new Exception("unauthorized access");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Transactional
    public void modify(int userId, String category, int categoryId) {
        CategoryEntity result = null;
        try {
            CategoryEntity ce= em.find(CategoryEntity.class,categoryId);
            ce.setName(category);
            em.merge(ce);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public ArrayList<Category> getAllByUsername(String username) {
        int id = userDao.getId(username);
        return getAllById(id);
    }
}
