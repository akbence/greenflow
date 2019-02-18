package dao.transactions;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import javax.persistence.EntityManager;


@Model
public class CategoryDao {

    @Inject
    private EntityManager em;
}
