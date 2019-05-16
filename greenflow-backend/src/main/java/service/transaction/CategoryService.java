package service.transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import converters.CategoryConverter;
import dao.UserDao;
import dao.transactions.CategoryDao;
import dao.transactions.TransactionDao;
import rest.Request.CategoryInput;
import rest.Response.CategoryResponse;
import service.authentication.LoggedInService;

import java.util.ArrayList;

@Model
public class CategoryService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private CategoryDao categoryDao;

    @Inject
    private UserDao userDao;

    @Inject
    TransactionService transactionService;

    @Inject
    CategoryConverter categoryConverter;

    public void post(CategoryInput categoryInput) throws Exception {
            Category category = new Category();
            category.setName(categoryInput.getName());
            category.setUsername(loggedInService.getCurrentUserName());
            categoryDao.addCategory(category);
    }

    public ArrayList<CategoryResponse> getAll() {
        int userId = userDao.getId(loggedInService.getCurrentUserName());
        ArrayList<Category> categories= categoryDao.getAllById(userId);
        ArrayList<CategoryResponse> responses = new ArrayList<>();
        for (Category category : categories) {
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setName(category.getName());
            categoryResponse.setId(category.getId());
            responses.add(categoryResponse);
        }
        return responses;
    }

    public void delete(int id) {
        int userId=userDao.getId(loggedInService.getCurrentUserName());
        try{
            transactionService.deleteAll(id);
            categoryDao.delete(userId,id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void modify(int categoryId,CategoryInput categoryInput) {
        int userId=userDao.getId(loggedInService.getCurrentUserName());
        categoryDao.modify(userId,categoryInput.getName(),categoryId);
    }
}
