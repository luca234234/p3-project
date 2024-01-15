import org.junit.Test;
import static org.junit.Assert.*;

public class AccountComparisonTest {

    @Test
    public void testCompareTo() {
        Account account1 = new Account("Account1", 500.0);
        Account account2 = new Account("Account2", 1000.0);

        assertTrue(account1.compareTo(account2) < 0);
        assertTrue(account2.compareTo(account1) > 0);
    }
}
