import java.util.Scanner;

public class BankManagementSystem {
    private static Bank bank = new Bank();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            showMenu();
            int choice = getChoice();
            switch (choice) {
                case 1:
                    createCustomer();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    withdrawMoney();
                    break;
                case 5:
                    checkBalance();
                    break;
                case 6:
                    displayAccountDetails();
                    break;
                case 7:
                    exit = true;
                    System.out.println("Exiting the system. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private static void showMenu() {
        System.out.println("\n--- Bank Management System Menu ---");
        System.out.println("1. Create Customer");
        System.out.println("2. Create Account");
        System.out.println("3. Deposit Money");
        System.out.println("4. Withdraw Money");
        System.out.println("5. Check Balance");
        System.out.println("6. Display Account Details");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getChoice() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            // Invalid input handled in main loop
        }
        return choice;
    }

    private static void createCustomer() {
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Customer Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Customer Phone Number: ");
        String phone = scanner.nextLine();
        LogWriter.append("CREATE_CUSTOMER", String.format("customerId=%s; name=%s; address=%s; phone=%s", customerId, name, address, phone));

        Customer customer = new Customer(customerId, name, address, phone);
        bank.addCustomer(customer);
    }

    private static void createAccount() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine();
        Customer customer = bank.findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found. Please create customer first.");
            return;
        }
        System.out.print("Enter Account Type (Savings/Current): ");
        String accountType = scanner.nextLine();
        LogWriter.append("CREATE_ACCOUNT", String.format("accountNumber=%s; customerId=%s; accountType=%s", accountNumber, customerId, accountType));

        Account account = new Account(accountNumber, customer.getName(), accountType);
        bank.addAccount(account);
        // Log initial balance after account creation
        LogWriter.append("ACCOUNT_INITIALIZED", String.format("accountNumber=%s; balance=%.2f", accountNumber, account.getBalance()));
    }

    private static void depositMoney() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = bank.findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.print("Enter amount to deposit: ");
        double amount = 0;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        account.deposit(amount);
        bank.saveAll(); // persist updated balance
        LogWriter.append("DEPOSIT", String.format("accountNumber=%s; amount=%.2f; balance=%.2f", accountNumber, amount, account.getBalance()));
    }

    private static void withdrawMoney() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = bank.findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.print("Enter amount to withdraw: ");
        double amount = 0;
        try {
            amount = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount.");
            return;
        }
        account.withdraw(amount);
        bank.saveAll(); // persist updated balance
        LogWriter.append("WITHDRAW", String.format("accountNumber=%s; amount=%.2f; balance=%.2f", accountNumber, amount, account.getBalance()));
    }

    private static void checkBalance() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = bank.findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        System.out.println("Current balance: " + account.getBalance());
        LogWriter.append("CHECK_BALANCE", String.format("accountNumber=%s; balance=%.2f", accountNumber, account.getBalance()));
    }

    private static void displayAccountDetails() {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();
        Account account = bank.findAccountByNumber(accountNumber);
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }
        account.displayAccountDetails();
    }
}
