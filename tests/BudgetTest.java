import org.junit.Test;
import static org.junit.Assert.*;

public class BudgetTest {

    @Test
    public void testBudgetConstructor() {
        Budget budget = new Budget(5000.0, "Monthly");
        assertEquals(5000.0, budget.getLimit(), 0.001);
        assertEquals("Monthly", budget.getPeriod());
    }
}
