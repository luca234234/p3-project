import org.junit.Test;
import static org.junit.Assert.*;

public class CategoryTest {

    @Test
    public void testCategoryConstructor() {
        Category category = new Category("Groceries");
        assertEquals("Groceries", category.getName());
        assertTrue(category.getTransactions().isEmpty());
    }
}

