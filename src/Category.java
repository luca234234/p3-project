import java.io.Serializable;
import java.util.ArrayList;

public class Category implements FinancialEntity, Serializable {
    private String name;
    private ArrayList<Transaction> transactions;

    public Category(String name) {
        this.name = name;
        this.transactions = new ArrayList<>();
    }

    // Getter and Setter
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
}
