import java.io.Serializable;

public class Transaction implements Comparable<Transaction>, Serializable {
    private double amount;
    private String date;
    private String description;
    private Category category;

    public Transaction(double amount, String date, String description, Category category) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
    }

    @Override
    public int compareTo(Transaction other) {
        return this.date.compareTo(other.date);
    }

    // Getters and Setters
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
