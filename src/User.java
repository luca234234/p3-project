import exceptions.DuplicateNameException;

import java.io.Serializable;
import java.util.ArrayList;
public class User implements Serializable {
    private Category preferredCategory;
    private ArrayList<Transaction> transactions;
    private ArrayList<Account> accounts;
    private ArrayList<Budget> budgets;
    private ArrayList<Category> categories;

    public User(Category preferredCategory) {
        this.preferredCategory = preferredCategory;
        this.transactions = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.budgets = new ArrayList<>();
        this.categories = new ArrayList<>();
        try {
            addCategory(preferredCategory);
        } catch (DuplicateNameException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Getters and Setters
    public Category getPreferredCategory() {
        return preferredCategory;
    }

    public void setPreferredCategory(Category preferredCategory) {
        this.preferredCategory = preferredCategory;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        Category category = new Category(transaction.getCategory().getName());
        category.addTransaction(transaction);
        try {
            addCategory(category);
        } catch (DuplicateNameException e) {
            System.out.println("Error: " + e.getMessage());
            for (Category catg : this.getCategories()) {
                if (catg.getName().equalsIgnoreCase(category.getName())) {
                    catg.addTransaction(transaction);
                }
            }
        }
        this.transactions.add(transaction);
    }
    public ArrayList<Account> getAccounts() {
        return accounts;
    }
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public ArrayList<Budget> getBudgets() {
        return budgets;
    }
    public void addBudget(Budget budget) {
        this.budgets.add(budget);
    }
    public ArrayList<Category> getCategories() {
        return categories;
    }
    public void addCategory(Category category) throws DuplicateNameException {
        for (Category catg : this.getCategories()) {
            if (catg.getName().equalsIgnoreCase(category.getName())) {
                throw new DuplicateNameException("Category with name " + catg.getName() + " already exists.");
            }
        }
        this.categories.add(category);
    }
}
