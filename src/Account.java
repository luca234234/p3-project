import java.io.Serializable;

public class Account implements FinancialEntity, Comparable<Account>, Serializable {
    private String name;
    private double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public int compareTo(Account other) {
        return Double.compare(this.balance, other.balance);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {return balance;}

}
