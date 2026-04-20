package atmsimulationproj;
import java.util.ArrayList;

public class Account {

    private int accountNumber;
    private int pin;
    private double balance;

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public Account(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public boolean validatePin(int inputPin) {
        return this.pin == inputPin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid deposit amount!");
        }
        balance += amount;
        transactions.add(new Transaction("DEPOSIT", amount));
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid withdrawal amount!");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient balance!");
        }
        balance -= amount;
        transactions.add(new Transaction("WITHDRAW", amount));
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
