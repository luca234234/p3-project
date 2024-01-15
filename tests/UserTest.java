import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {
    @Test
    public void testUserConstructor() {
        Category preferredCategory = new Category("General");
        User user = new User("testUser", "password123", preferredCategory);
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals(preferredCategory, user.getPreferredCategory());
        assertTrue(user.getTransactions().isEmpty());
    }
}
