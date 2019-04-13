package converters;

import entities.transactions.CategoryEntity;
import rest.Response.CategoryResponse;
import service.transaction.Category;

import javax.enterprise.inject.Model;
import java.util.ArrayList;

@Model
public class CategoryConverter {

    public Category daoToService(CategoryEntity categoryEntity){
        Category category = new Category();
        category.setName(categoryEntity.getName());
        category.setId(categoryEntity.getId());
        return category;
    }

    public ArrayList<Category> daoToServiceList(ArrayList<CategoryEntity> categoryEntities){
        ArrayList<Category> categories = new ArrayList<>();
        categoryEntities.forEach(categoryEntity -> categories.add(daoToService(categoryEntity)));
        return categories;
    }

}
