package service.transaction;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import dao.transactions.CategoryDao;
import rest.Input.CategoryInput;
import service.authentication.LoggedInService;

@Model
public class CategoryService {

    @Inject
    private LoggedInService loggedInService;

    @Inject
    private CategoryDao categoryDao;

    public void post(CategoryInput categoryInput) throws Exception {
        if (loggedInService.isLoggedIn()) {
            loggedInService.checkToken(categoryInput.getToken());

            Category category = new Category();
            category.setName(categoryInput.getName());
            category.setUsername(loggedInService.getCurrentUserName());

            categoryDao.post(category);

        }

    }

}
