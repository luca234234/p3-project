import exceptions.InvalidTransactionException;

import java.io.*;
import java.util.Scanner;
import java.util.*;

public class Main {
    private static final String DATA_FILE = "finance_tracker_data.dat";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = null;

        System.out.println("Do you want to load previous data? (yes/no)");
        String answer = scanner.nextLine();
        if ("yes".equalsIgnoreCase(answer)) {
            try {
                user = DataPersistence.loadUserData(DATA_FILE);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error loading data: " + e.getMessage());
                user = new User(new Category("General"));
            }
        } else {
            user = new User(new Category("General"));
        }
        if (args.length > 0) {
            if ("add".equals(args[0])) {
                try {
                    addTransaction(user);
                } catch (InvalidTransactionException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                try {
                    DataPersistence.saveUserData(user, DATA_FILE);
                } catch (IOException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                }
            } else if ("summary".equals(args[0])) {
                summarizeTransactions(user);
            }
        }
    }

    public static void addTransaction(User user) throws InvalidTransactionException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter transaction amount:");
        String amountInput = scanner.nextLine();
        double amount;
        try {
             amount = Double.parseDouble(amountInput);
        } catch (NumberFormatException e) {
            throw new InvalidTransactionException("Invalid input: Amount must be a number.");
        }

        System.out.println("Enter transaction date (e.g., 2023-11-11):");
        String date = scanner.nextLine();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new InvalidTransactionException("Invalid date format. Use YYYY-MM-DD.");
        }

        System.out.println("Enter transaction description:");
        String description = scanner.nextLine();
        if (description.isEmpty()) {
            throw new InvalidTransactionException("Description cannot be empty.");
        }

        System.out.println("Enter transaction category:");
        String categoryName = scanner.nextLine();
        if (categoryName.isEmpty()) {
            throw new InvalidTransactionException("Category cannot be empty.");
        }
        Category category = new Category(categoryName);

        Transaction newTransaction = new Transaction(amount, date, description, category);
        user.addTransaction(newTransaction);

        System.out.println("Transaction added successfully.");
    }
    public static void summarizeTransactions(User user) {
        double totalIncome = 0;
        double totalExpenses = 0;
        int transactionCount = 0;

        for (Transaction transaction : user.getTransactions()) {
            if (transaction != null) {
                transactionCount++;
                if (transaction.getAmount() > 0) {
                    totalIncome += transaction.getAmount();
                } else {
                    totalExpenses += transaction.getAmount();
                }
            }
        }

        System.out.println("Total Transactions: " + transactionCount);
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenses);
    }

    public static void sortTransactionsByDate(ArrayList<Transaction> transactions) {
        Collections.sort(transactions);
    }
    public static void sortAccountsByBalance(ArrayList<Account> accounts) {
        Collections.sort(accounts);
    }
    public static Map<String, List<Transaction>> groupTransactionsByCategory(List<Transaction> transactions) {
        Map<String, List<Transaction>> groupedTransactions = new HashMap<>();
        for (Transaction transaction : transactions) {
            String category = transaction.getCategory().getName();

            groupedTransactions.computeIfAbsent(category, k -> new ArrayList<>()).add(transaction);
        }
        return groupedTransactions;
    }
}
