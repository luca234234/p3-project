import exceptions.InvalidTransactionException;

import java.io.*;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.*;

public class Main {
    static int MAX_USERNAME_LENGTH = 50;
    static int MAX_PASSWORD_LENGTH = 50;
    static int MAX_USERTYPE_LENGTH = 20;
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        User user = null;
        boolean exit = false;

        while (!exit) {
            System.out.println("Do you have an account? (yes/no)");
            String answer = scanner.nextLine();

            if ("yes".equalsIgnoreCase(answer)) {
                boolean loginSuccessful = false;
                while (!loginSuccessful) {
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();

                    if (username.isEmpty() || username.length() > MAX_USERNAME_LENGTH) {
                        System.out.println("Username must be between 1 and " + MAX_USERNAME_LENGTH + " characters. Please try again.");
                        continue;
                    }

                    System.out.println("Enter password:");
                    String password = scanner.nextLine();

                    if (password.isEmpty() || password.length() > MAX_PASSWORD_LENGTH) {
                        System.out.println("Password must be between 1 and " + MAX_PASSWORD_LENGTH + " characters. Please try again.");
                        continue;
                    }
                    try {
                        user = DataPersistence.authenticateUser(username, password);
                        if (user != null) {
                            loginSuccessful = true;
                            break;
                        } else {
                            System.out.println("Invalid username or password. Try again? (yes/no)");
                            if ("no".equalsIgnoreCase(scanner.nextLine())) {
                                break;
                            }
                        }
                    } catch (IOException | ClassNotFoundException | SQLException e) {
                        System.out.println("Error during authentication: " + e.getMessage());
                        break;
                    }
                }
                if (loginSuccessful) {
                    break;
                }
            } else if ("no".equalsIgnoreCase(answer)) {
                boolean validInput = false;
                while (!validInput) {
                    System.out.println("Create a new account:");
                    System.out.println("Enter new username:");
                    String newUsername = scanner.nextLine();
                    try {
                        if (newUsername.isEmpty() || newUsername.length() > MAX_USERNAME_LENGTH) {
                            System.out.println("Username must be between 1 and " + MAX_USERNAME_LENGTH + " characters. Please try again.");
                            continue;
                        } else if (DataPersistence.usernameExists(newUsername)) {
                            System.out.println("Username already exists. Please try a different username.");
                        }
                    } catch (SQLException e) {
                        System.out.println("A database error occurred: " + e.getMessage());
                        continue;
                    }
                    System.out.println("Enter new password:");
                    String newPassword = scanner.nextLine();
                    if (newPassword.isEmpty() || newPassword.length() > MAX_PASSWORD_LENGTH) {
                        System.out.println("Password must be between 1 and " + MAX_PASSWORD_LENGTH + " characters. Please try again.");
                        continue;
                    }
                    validInput = true;
                    user = new User(newUsername, newPassword, new Category("General"));
                    try {
                        DataPersistence.saveUserData(user);
                    } catch (IOException | SQLException e) {
                        System.out.println("Error saving data: " + e.getMessage());
                    }
                }
                exit = true;
            } else {
                System.out.println("Invalid option. Please try again.");
            }
        }
        if (args.length > 0) {
            if ("add".equals(args[0])) {
                try {
                    addTransaction(user);
                } catch (InvalidTransactionException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                try {
                    DataPersistence.saveUserData(user);
                } catch (IOException | SQLException e) {
                    System.out.println("Error saving data: " + e.getMessage());
                }
            } else if ("summary".equals(args[0])) {
                summarizeTransactions(user);
            }
        }
        else if ("admin".equalsIgnoreCase(user.getUserType())) {
            boolean exitAdminMenu = false;
            while (!exitAdminMenu) {
                System.out.println("Select action: 1) Manage Users 2) Add Transaction 3) Summary 4) Exit");
                int action = scanner.nextInt();
                switch (action) {
                    case 1:
                        manageUsers();
                        break;
                    case 2:
                        try {
                            addTransaction(user);
                        } catch (InvalidTransactionException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        try {
                            DataPersistence.saveUserData(user);
                        } catch (IOException | SQLException e) {
                            System.out.println("Error saving data: " + e.getMessage());
                        }
                        break;
                    case 3:
                        summarizeTransactions(user);
                        break;
                    case 4:
                        exitAdminMenu = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } else{
            boolean exit_menu = false;
            while (!exit_menu) {
                System.out.println("Select action: 1) Add Transaction 2) Summary 3) Exit");
                int action = scanner.nextInt();
                switch (action) {
                    case 1:
                        try {
                            addTransaction(user);
                        } catch (InvalidTransactionException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        try {
                            DataPersistence.saveUserData(user);
                        } catch (IOException | SQLException e) {
                            System.out.println("Error saving data: " + e.getMessage());
                        }
                        break;
                    case 2:
                        summarizeTransactions(user);
                        break;
                    case 3:
                        exit_menu = true;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
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
    private static void manageUsers() {
        Scanner scanner = new Scanner(System.in);
        boolean exitUserManagement = false;

        while (!exitUserManagement) {
            System.out.println("User Management: 1) List Users 2) Update User 3) Back to Main Menu");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    try {
                        List<String[]> users = DataPersistence.listAllUsers();
                        for (String[] user : users) {
                            System.out.println("Username: " + user[0] + ", Type: " + user[1]);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Error accessing database: " + e.getMessage());
                    }
                    break;
                case 2:
                    boolean validInput = false;
                    while (!validInput) {
                        System.out.println("Enter the old username:");
                        String oldUsername = scanner.next();
                        if (oldUsername.isEmpty()|| oldUsername.length() > MAX_USERNAME_LENGTH) {
                            System.out.println("Username must be between 1 and " + MAX_USERNAME_LENGTH + " characters. Please try again.");
                            continue;
                        }
                        System.out.println("Enter the new username:");
                        String newUsername = scanner.next();
                        try {
                            if (newUsername.isEmpty() || newUsername.length() > MAX_USERNAME_LENGTH) {
                                System.out.println("Username must be between 1 and " + MAX_USERNAME_LENGTH + " characters. Please try again.");
                                continue;
                            } else if (!newUsername.equals(oldUsername) && DataPersistence.usernameExists(newUsername)) {
                                System.out.println("Username already exists. Please try a different username.");
                                continue;
                            }
                        } catch (SQLException e) {
                            System.out.println("A database error occurred: " + e.getMessage());
                        }
                        System.out.println("Enter the new password:");
                        String newPassword = scanner.next();
                        if (newPassword.isEmpty() || newPassword.length() > MAX_PASSWORD_LENGTH) {
                            System.out.println("Password must be between 1 and " + MAX_PASSWORD_LENGTH + " characters. Please try again.");
                            continue;
                        }
                        System.out.println("Enter the new user type:");
                        String userType = scanner.next();
                        if (userType.isEmpty() || userType.length() > MAX_USERTYPE_LENGTH) {
                            System.out.println("User type must be between 1 and " + MAX_USERTYPE_LENGTH + " characters. Please try again.");
                            continue;
                        }
                        validInput = true;
                        try {
                            DataPersistence.updateUserDetails(oldUsername, newUsername, newPassword, userType);
                            System.out.println("User updated successfully.");
                        } catch (SQLException e) {
                            System.out.println("Error updating user: " + e.getMessage());
                        }
                    }
                    break;
                case 3:
                    exitUserManagement = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
