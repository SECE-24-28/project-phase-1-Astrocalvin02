import java.io.*;
import java.nio.file.*;
import java.util.*;

// Simple CSV-based persistence for Customers and Accounts
public class DataStore {
    private final Path baseDir;
    private final Path customersFile;
    private final Path accountsFile;

    public DataStore(String baseDirectory) {
        this.baseDir = Paths.get(baseDirectory);
        this.customersFile = baseDir.resolve("customers.csv");
        this.accountsFile = baseDir.resolve("accounts.csv");
        ensureFiles();
    }

    private void ensureFiles() {
        try {
            if (!Files.exists(baseDir)) {
                Files.createDirectories(baseDir);
            }
            if (!Files.exists(customersFile)) {
                Files.createFile(customersFile);
                // header
                Files.write(customersFile, Arrays.asList("customerId,name,address,phone"), StandardOpenOption.TRUNCATE_EXISTING);
            }
            if (!Files.exists(accountsFile)) {
                Files.createFile(accountsFile);
                // header
                Files.write(accountsFile, Arrays.asList("accountNumber,customerName,accountType,balance"), StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize datastore: " + e.getMessage(), e);
        }
    }

    // ---------- Customers ----------
    public List<Customer> loadCustomers() {
        List<Customer> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(customersFile)) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsv(line, 4);
                String id = parts[0];
                String name = parts[1];
                String addr = parts[2];
                String phone = parts[3];
                list.add(new Customer(id, name, addr, phone));
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not load customers: " + e.getMessage());
        }
        return list;
    }

    public void appendCustomer(Customer c) {
        String line = toCsv(c.getCustomerId(), c.getName(), c.getAddress(), c.getPhoneNumber());
        appendLine(customersFile, line);
    }

    public void saveAllCustomers(List<Customer> customers) {
        List<String> lines = new ArrayList<>();
        lines.add("customerId,name,address,phone");
        for (Customer c : customers) {
            lines.add(toCsv(c.getCustomerId(), c.getName(), c.getAddress(), c.getPhoneNumber()));
        }
        writeAll(customersFile, lines);
    }

    // ---------- Accounts ----------
    public List<Account> loadAccounts() {
        List<Account> list = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(accountsFile)) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) { headerSkipped = true; continue; }
                if (line.trim().isEmpty()) continue;
                String[] parts = parseCsv(line, 4);
                String number = parts[0];
                String customerName = parts[1];
                String type = parts[2];
                double balance = Double.parseDouble(parts[3]);
                Account a = new Account(number, customerName, type);
                // set initial balance by depositing for simplicity
                if (balance > 0) a.deposit(balance);
                list.add(a);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Warning: Could not load accounts: " + e.getMessage());
        }
        return list;
    }

    public void appendAccount(Account a) {
        String line = toCsv(a.getAccountNumber(), a.getCustomerName(), a.getAccountType(), String.valueOf(a.getBalance()));
        appendLine(accountsFile, line);
    }

    public void saveAllAccounts(List<Account> accounts) {
        List<String> lines = new ArrayList<>();
        lines.add("accountNumber,customerName,accountType,balance");
        for (Account a : accounts) {
            lines.add(toCsv(a.getAccountNumber(), a.getCustomerName(), a.getAccountType(), String.valueOf(a.getBalance())));
        }
        writeAll(accountsFile, lines);
    }

    // ---------- Helpers ----------
    private void appendLine(Path file, String line) {
        try {
            Files.write(file, Arrays.asList(line), StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Warning: Could not persist data: " + e.getMessage());
        }
    }

    private void writeAll(Path file, List<String> lines) {
        try {
            Files.write(file, lines, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.out.println("Warning: Could not write data file: " + e.getMessage());
        }
    }

    private String toCsv(String... fields) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(escape(fields[i] == null ? "" : fields[i]));
        }
        return sb.toString();
    }

    private String[] parseCsv(String line, int expected) {
        // Minimal CSV parser for commas and quotes
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') { // escaped quote
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(ch);
                }
            } else {
                if (ch == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (ch == '"') {
                    inQuotes = true;
                } else {
                    cur.append(ch);
                }
            }
        }
        out.add(cur.toString());
        while (out.size() < expected) out.add("");
        return out.toArray(new String[0]);
    }

    private String escape(String value) {
        boolean needQuotes = value.contains(",") || value.contains("\n") || value.contains("\r") || value.contains("\"");
        String v = value.replace("\"", "\"\"");
        if (needQuotes) {
            return '"' + v + '"';
        }
        return v;
    }
}