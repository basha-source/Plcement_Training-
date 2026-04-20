package atmsimulationproj;

import java.util.HashMap;
import java.util.Scanner;

public class ATM {

    private HashMap<Integer, Account> accounts = new HashMap<>();
    private Scanner sc = new Scanner(System.in);

    public ATM() {
        // Sample Accounts
        accounts.put(1001, new Account(1001, 1234, 5000));
        accounts.put(1002, new Account(1002, 2222, 8000));
    }

    public void start() {
        System.out.println("===== ATM SYSTEM =====");

        System.out.print("Enter Account Number: ");
        int accNum = sc.nextInt();

        System.out.print("Enter PIN: ");
        int pin = sc.nextInt();

        if (!accounts.containsKey(accNum) || !accounts.get(accNum).validatePin(pin)) {
            System.out.println("Invalid Credentials!");
            return;
        }

        Account acc = accounts.get(accNum);
        System.out.println("Login Successful!\n");

        int choice;
        do {
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transaction History");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Current Balance: ₹" + acc.getBalance());
                    break;

                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double dep = sc.nextDouble();
                    try {
                        acc.deposit(dep);
                        System.out.println("Deposit Successful!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double wd = sc.nextDouble();
                    try {
                        acc.withdraw(wd);
                        System.out.println("Withdrawal Successful!");
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.println("----- Transaction History -----");
                    for (Transaction t : acc.getTransactions()) {
                        System.out.println(t);
                    }
                    break;

                case 5:
                    System.out.println("Thank you for using the ATM!");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

            System.out.println();

        } while (choice != 5);
    }

    public static void main(String[] args) {
        new ATM().start();
    }
}
