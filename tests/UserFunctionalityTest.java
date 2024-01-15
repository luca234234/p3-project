import org.junit.Test;
import static org.junit.Assert.*;

public class UserFunctionalityTest {

    @Test
    public void testAddTransaction() {
        User user = new User("testUser", "password123", new Category("General"));
        Transaction transaction = new Transaction(100.0, "2024-01-01", "Test Transaction", new Category("General"));

        user.addTransaction(transaction);
        assertFalse(user.getTransactions().isEmpty());
        assertEquals(1, user.getTransactions().size());
    }
}


