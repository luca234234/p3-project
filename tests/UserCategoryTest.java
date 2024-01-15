import exceptions.DuplicateNameException;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserCategoryTest {

    @Test
    public void testAddCategory() throws DuplicateNameException {
        User user = new User("testUser", "password123", new Category("General"));
        Category newCategory = new Category("Entertainment");

        user.addCategory(newCategory);
        assertEquals(2, user.getCategories().size());
    }

    @Test(expected = DuplicateNameException.class)
    public void testAddDuplicateCategory() throws DuplicateNameException {
        User user = new User("testUser", "password123", new Category("General"));
        Category duplicateCategory = new Category("General");
        user.addCategory(duplicateCategory);
    }
}

