import java.util.ArrayList;
import java.util.List;

public class Bank {
    private ArrayList<Account> accounts;
    private ArrayList<Customer> customers;
    private final DataStore dataStore;

    // Initialize with a DataStore and load existing data
    public Bank(DataStore dataStore) {
        this.dataStore = dataStore;
        List<Customer> loadedCustomers = dataStore.loadCustomers();
        List<Account> loadedAccounts = dataStore.loadAccounts();
        this.customers = new ArrayList<>(loadedCustomers);
        this.accounts = new ArrayList<>(loadedAccounts);
    }

    // Backward-compatible constructor (in case of older calls)
    // Defaults to in-memory only without persistence
    public Bank() {
        this(new DataStore("e:\\Java-programming-main\\java_project\\data"));
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
        dataStore.appendCustomer(customer); // persist incrementally
        System.out.println("Customer added successfully.");
    }
    
    public Customer findCustomerById(String customerId) {
        for (Customer c : customers) {
            if (c.getCustomerId().equals(customerId)) {
                return c;
            }
        }
        return null;
    }

    public void addAccount(Account account) {
        accounts.add(account);
        dataStore.appendAccount(account); // persist incrementally
        System.out.println("Account created successfully.");
    }

    public Account findAccountByNumber(String accountNumber) {
        for (Account a : accounts) {
            if (a.getAccountNumber().equals(accountNumber)) {
                return a;
            }
        }
        return null;
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }
        for (Account a : accounts) {
            a.displayAccountDetails();
            System.out.println("-----------------------");
        }
    }

    // Save all data (useful after balance changes like deposit/withdraw)
    public void saveAll() {
        dataStore.saveAllCustomers(customers);
        dataStore.saveAllAccounts(accounts);
    }

    // Expose lists if needed (read-only copies)
    public List<Account> getAccounts() { return new ArrayList<>(accounts); }
    public List<Customer> getCustomers() { return new ArrayList<>(customers); }
}