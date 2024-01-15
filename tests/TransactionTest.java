import org.junit.Test;
import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void testTransactionConstructor() {
        Transaction transaction = new Transaction(200.0, "2024-01-01", "Grocery shopping", new Category("Groceries"));
        assertEquals(200.0, transaction.getAmount(), 0.001);
        assertEquals("2024-01-01", transaction.getDate());
        assertEquals("Grocery shopping", transaction.getDescription());
        assertNotNull(transaction.getCategory());
        assertEquals("Groceries", transaction.getCategory().getName());
    }
}

