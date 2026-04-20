import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// ATM class FIRST (has main) - for online compilers
public class ATM {
    private Account account;
    private Scanner scanner;
    private boolean authenticated = false;

    public ATM() {
        scanner = new Scanner(System.in);
        account = new Account("123456", "1234", 1000.0);
    }

    public void run() {
        authenticate();
        if (authenticated) {
            showMenu();
        }
        scanner.close();
    }

    private void authenticate() {
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();
        if (!accNum.equals(account.getAccountNumber())) {
            System.out.println("Invalid account number.");
            return;
        }
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        if (account.validatePin(pin)) {
            System.out.println("Login successful!");
            authenticated = true;
        } else {
            System.out.println("Invalid PIN.");
        }
    }

    private void showMenu() {
        int choice;
        do {
            System.out.println("\nATM Menu:");
            System.out.println("1. Balance Inquiry");
            System.out.println("2. Withdrawal");
            System.out.println("3. Deposit");
            System.out.println("4. Transaction History");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            Transaction transaction = null;
            switch (choice) {
                case 1: transaction = new BalanceInquiry(account, scanner); break;
                case 2: transaction = new Withdrawal(account, scanner); break;
                case 3: transaction = new Deposit(account, scanner); break;
                case 4: account.printHistory(); continue;
                case 5: System.out.println("Thank you!"); break;
                default: System.out.println("Invalid.");
            }
            if (transaction != null) {
                transaction.execute();
            }
        } while (choice != 5);
    }

    public static void main(String[] args) {
        new ATM().run();
    }
}

// Encapsulated Account class (now after ATM)
class Account {
    private String accountNumber;
    private String pin;
    private double balance;
    private List<String> transactionHistory = new ArrayList<>();

    public Account(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
    }

    public boolean validatePin(String inputPin) {
        return pin.equals(inputPin);
    }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public String getAccountNumber() { return accountNumber; }

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
        System.out.println(transaction);
    }

    public void printHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions.");
        } else {
            System.out.println("History:");
            for (String t : transactionHistory) {
                System.out.println(t);
            }
        }
    }
}

// Abstract Transaction (Abstraction)
abstract class Transaction {
    protected Account account;
    protected Scanner scanner;

    public Transaction(Account account, Scanner scanner) {
        this.account = account;
        this.scanner = scanner;
    }

    public abstract void execute();
}

// Inheritance + Polymorphism
class BalanceInquiry extends Transaction {
    public BalanceInquiry(Account a, Scanner s) { super(a, s); }
    public void execute() {
        account.addTransaction("Balance: $" + account.getBalance());
    }
}

class Withdrawal extends Transaction {
    public Withdrawal(Account a, Scanner s) { super(a, s); }
    public void execute() {
        System.out.print("Amount: $");
        double amt = scanner.nextDouble();
        if (amt > 0 && amt <= account.getBalance()) {
            account.setBalance(account.getBalance() - amt);
            account.addTransaction("Withdrew $" + amt + ". Balance: $" + account.getBalance());
        } else {
            System.out.println("Invalid/Insufficient funds.");
        }
    }
}

class Deposit extends Transaction {
    public Deposit(Account a, Scanner s) { super(a, s); }
    public void execute() {
        System.out.print("Amount: $");
        double amt = scanner.nextDouble();
        if (amt > 0) {
            account.setBalance(account.getBalance() + amt);
            account.addTransaction("Deposited $" + amt + ". Balance: $" + account.getBalance());
        } else {
            System.out.println("Invalid.");
        }
    }
}
