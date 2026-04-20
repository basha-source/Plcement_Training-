import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Main class FIRST for online compilers
public class EmployeeManagement {
    private static List<Employee> employees = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static int nextId = 192324104;  // Starting ID

    public static void main(String[] args) {
        System.out.println("=== Employee Management System ===");
        int choice;
        do {
            showMenu();
            choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: addEmployee(); break;
                case 2: viewAll(); break;
                case 3: computeSalaries(); break;
                case 4: searchEmployee(); break;
                case 5: System.out.println("Exiting..."); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 5);
    }

    private static void showMenu() {
        System.out.println("\n1. Add Employee");  // FIXED: \\n → \n
        System.out.println("2. View All Employees");
        System.out.println("3. Compute All Salaries");
        System.out.println("4. Search by ID");
        System.out.println("5. Exit");
        System.out.print("Choose: ");
    }

    private static void addEmployee() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Type (1=Manager, 2=Developer): ");
        int type = scanner.nextInt();
        scanner.nextLine();

        Employee emp;
        if (type == 1) {
            System.out.print("Bonus: $");
            double bonus = scanner.nextDouble();
            emp = new Manager(nextId++, name, bonus);  // Auto-increments to 192324105 next
        } else {
            System.out.print("Hourly Rate: $");
            double rate = scanner.nextDouble();
            System.out.print("Hours Worked: ");
            int hours = scanner.nextInt();
            emp = new Developer(nextId++, name, rate, hours);
        }
        employees.add(emp);
        System.out.println("Added: " + emp.getName() + " (ID: " + emp.getId() + ")");
    }

    private static void viewAll() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        for (Employee e : employees) {
            e.display();
            System.out.println("---");
        }
    }

    private static void computeSalaries() {
        if (employees.isEmpty()) {
            System.out.println("No employees.");
            return;
        }
        System.out.println("Salaries:");
        for (Employee e : employees) {
            System.out.println("ID " + e.getId() + " - " + e.getName() + ": $" + e.calculateSalary());
        }
    }

    private static void searchEmployee() {
        System.out.print("ID: ");
        int id = scanner.nextInt();
        for (Employee e : employees) {
            if (e.getId() == id) {
                e.display();
                return;
            }
        }
        System.out.println("Not found.");
    }
}

// Abstract base class (Abstraction)
abstract class Employee {
    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    // Abstract methods for polymorphism
    public abstract double calculateSalary();
    public abstract void display();
}

// Manager: Inheritance + Override (Polymorphism)
class Manager extends Employee {
    private double bonus;

    public Manager(int id, String name, double bonus) {
        super(id, name);
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return 50000 + bonus;  // Base + bonus
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId() + ", Name: " + getName() + ", Type: Manager, Bonus: $" + bonus);
    }
}

// Developer: Inheritance + Override
class Developer extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public Developer(int id, String name, double rate, int hours) {
        super(id, name);
        this.hourlyRate = rate;
        this.hoursWorked = hours;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public void display() {
        System.out.println("ID: " + getId() + ", Name: " + getName() + 
                          ", Type: Developer, Rate: $" + hourlyRate + ", Hours: " + hoursWorked);
    }
}
