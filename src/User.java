import exceptions.DuplicateNameException;

import java.io.Serializable;
import java.util.ArrayList;
public class User implements Serializable {
    private String username;
    private String password;
    private String userType;
    private Category preferredCategory;
    private ArrayList<Transaction> transactions;
    private ArrayList<Account> accounts;
    private ArrayList<Budget> budgets;
    private ArrayList<Category> categories;

    public User(String username, String password, Category preferredCategory) {
        this.username = username;
        this.password = password;
        this.userType = "user";
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
    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {this.userType = userType;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {this.password = password;}
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
