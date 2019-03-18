package service.transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import dao.UserDao;
import dao.transactions.CategoryDao;
import rest.Input.CategoryInput;
import rest.Input.UserAuthInput;
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

    public void post(CategoryInput categoryInput) throws Exception {
        if (loggedInService.isLoggedIn()) {
            loggedInService.checkToken(categoryInput.getToken());

            Category category = new Category();
            category.setName(categoryInput.getName());
            category.setUsername(loggedInService.getCurrentUserName());

            categoryDao.post(category);

        }

    }

    public ArrayList<String> getAll() {
        int userId = userDao.getId(loggedInService.getCurrentUserName());
        return categoryDao.getAllById(userId);
    }
}
