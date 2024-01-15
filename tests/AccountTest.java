import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void testAccountConstructor() {
        Account account = new Account("TestAccount", 1000.0);
        assertEquals("TestAccount", account.getName());
        assertEquals(1000.0, account.getBalance(), 0.001);
    }
}
