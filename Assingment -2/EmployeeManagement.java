package mini.project2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeManagementSystem {

 private ArrayList<Employee> employees = new ArrayList<>();
 private HashMap<String, Employee> employeeMap = new HashMap<>();
 private static final String FILE_NAME = "employees.dat";
 private final Scanner scanner = new Scanner(System.in);

 public static void main(String[] args) {
     EmployeeManagementSystem system = new EmployeeManagementSystem();
     system.loadFromFile();
     system.runMenu();
 }

 private void runMenu() {
     while (true) {
         printMenu();
         try {
             int choice = Integer.parseInt(scanner.nextLine().trim());
             switch (choice) {
                 case 1 -> addEmployee();
                 case 2 -> viewAllEmployees();
                 case 3 -> searchByDepartment();
                 case 4 -> sortBySalary();
                 case 5 -> updateEmployee();
                 case 6 -> deleteEmployee();
                 case 7 -> exportToCSV();
                 case 8 -> {
                     saveToFile();
                     System.out.println("Thank you! Data saved!");
                     return;
                 }
                 default -> System.out.println("Invalid choice! Please enter 1-8.");
             }
         } catch (NumberFormatException e) {
             System.out.println("Please enter a valid number!");
         } catch (Exception e) {
             System.out.println("Error: " + e.getMessage());
         }
     }
 }

 private void printMenu() {
     System.out.println("\n" + "=".repeat(50));
     System.out.println("          EMPLOYEE MANAGEMENT SYSTEM");
     System.out.println("=".repeat(50));
     System.out.println("1. Add Employee");
     System.out.println("2. View All Employees");
     System.out.println("3. Search by Department");
     System.out.println("4. Sort by Salary");
     System.out.println("5. Update Employee");
     System.out.println("6. Delete Employee");
     System.out.println("7. Export to CSV");
     System.out.println("8. Exit");
     System.out.println("=".repeat(50));
     System.out.print("Enter your choice: ");
 }

 // ==================== ADD EMPLOYEE ====================
 private void addEmployee() throws InvalidEmployeeDataException, DuplicateEmployeeException {
     System.out.println("\n--- Add New Employee ---");

     System.out.print("Enter Employee ID: ");
     String id = scanner.nextLine().trim();
     if (id.isEmpty()) throw new InvalidEmployeeDataException("Employee ID cannot be empty!");

     if (employeeMap.containsKey(id)) {
         throw new DuplicateEmployeeException("Employee with ID '" + id + "' already exists!");
     }

     System.out.print("Enter Name: ");
     String name = scanner.nextLine().trim();
     if (name.isEmpty()) throw new InvalidEmployeeDataException("Name cannot be empty!");

     System.out.print("Enter Department: ");
     String department = scanner.nextLine().trim();
     if (department.isEmpty()) throw new InvalidEmployeeDataException("Department cannot be empty!");

     System.out.print("Enter Salary: ");
     String salaryStr = scanner.nextLine().trim();
     double salary;
     try {
         salary = Double.parseDouble(salaryStr);
         if (salary <= 0) {
             throw new InvalidEmployeeDataException("Salary must be greater than zero!");
         }
     } catch (NumberFormatException e) {
         throw new InvalidEmployeeDataException("Invalid salary format! Please enter a number.");
     }

     Employee emp = new Employee(id, name, department, salary);
     employees.add(emp);
     employeeMap.put(id, emp);

     System.out.println("Employee added successfully!");
     saveToFile();
 }

 // ==================== VIEW ALL ====================
 private void viewAllEmployees() {
     if (employees.isEmpty()) {
         System.out.println("No employees found!");
         return;
     }
     System.out.println("\n--- All Employees (" + employees.size() + ") ---");
     employees.forEach(System.out::println);   // Lambda
 }

 // ==================== SEARCH BY DEPARTMENT ====================
 private void searchByDepartment() {
     System.out.print("\nEnter Department to search: ");
     String dept = scanner.nextLine().trim();

     List<Employee> result = employees.stream()
             .filter(e -> e.getDepartment().equalsIgnoreCase(dept))
             .collect(Collectors.toList());

     if (result.isEmpty()) {
         System.out.println("No employees found in department: " + dept);
     } else {
         System.out.println("\n--- Employees in " + dept + " (" + result.size() + ") ---");
         result.forEach(System.out::println);
     }
 }

 // ==================== SORT BY SALARY ====================
 private void sortBySalary() {
     if (employees.isEmpty()) {
         System.out.println("No employees to sort!");
         return;
     }

     System.out.print("Sort by salary (1: Ascending, 2: Descending): ");
     int choice = Integer.parseInt(scanner.nextLine().trim());

     Comparator<Employee> comparator = Comparator.comparingDouble(Employee::getSalary);
     if (choice == 2) comparator = comparator.reversed();

     List<Employee> sorted = employees.stream()
             .sorted(comparator)
             .collect(Collectors.toList());

     System.out.println("\n--- Employees Sorted by Salary ---");
     sorted.forEach(System.out::println);
 }

 // ==================== UPDATE EMPLOYEE ====================
 private void updateEmployee() throws EmployeeNotFoundException, InvalidEmployeeDataException {
     System.out.print("\nEnter Employee ID to update: ");
     String id = scanner.nextLine().trim();

     Optional<Employee> optionalEmp = findById(id);
     if (optionalEmp.isEmpty()) {
         throw new EmployeeNotFoundException("Employee with ID '" + id + "' not found!");
     }

     Employee emp = optionalEmp.get();
     System.out.println("Current details: " + emp);

     // Name
     System.out.print("New Name (press Enter to keep current): ");
     String name = scanner.nextLine().trim();
     if (!name.isEmpty()) emp.setName(name);

     // Department
     System.out.print("New Department (press Enter to keep current): ");
     String dept = scanner.nextLine().trim();
     if (!dept.isEmpty()) emp.setDepartment(dept);

     // Salary
     System.out.print("New Salary (press Enter to keep current): ");
     String salStr = scanner.nextLine().trim();
     if (!salStr.isEmpty()) {
         try {
             double newSalary = Double.parseDouble(salStr);
             if (newSalary > 0) {
                 emp.setSalary(newSalary);
             } else {
                 System.err.println("Salary must be > 0. Keeping previous value.");
             }
         } catch (NumberFormatException e) {
             System.err.println("Invalid salary. Keeping previous value.");
         }
     }

     System.out.println("Employee updated successfully!");
     saveToFile();
 }

 // ==================== DELETE EMPLOYEE ====================
 private void deleteEmployee() throws EmployeeNotFoundException {
     System.out.print("\nEnter Employee ID to delete: ");
     String id = scanner.nextLine().trim();

     Optional<Employee> optionalEmp = findById(id);
     if (optionalEmp.isEmpty()) {
         throw new EmployeeNotFoundException("Employee with ID '" + id + "' not found!");
     }

     Employee emp = optionalEmp.get();
     employees.remove(emp);
     employeeMap.remove(id);

     System.out.println("Employee deleted successfully!");
     saveToFile();
 }

 // ==================== EXPORT TO CSV ====================
 private void exportToCSV() {
     if (employees.isEmpty()) {
         System.out.println("Nothing to export!");
         return;
     }

     String csvFile = "employees_export.csv";
     try (PrintWriter pw = new PrintWriter(new FileWriter(csvFile))) {
         pw.println("ID,Name,Department,Salary");

         // Stream + Lambda + method reference
         employees.stream()
                 .map(e -> e.getId() + "," + e.getName() + "," + e.getDepartment() + "," + e.getSalary())
                 .forEach(pw::println);

         System.out.println("Exported successfully to " + csvFile);
     } catch (IOException e) {
         System.err.println("Error exporting CSV: " + e.getMessage());
     }
 }

 // ==================== HELPER: FIND BY ID (Optional) ====================
 private Optional<Employee> findById(String id) {
     return Optional.ofNullable(employeeMap.get(id));
 }

 // ==================== FILE PERSISTENCE ====================
 private void loadFromFile() {
     try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
         @SuppressWarnings("unchecked")
         ArrayList<Employee> loaded = (ArrayList<Employee>) ois.readObject();

         employees = loaded;
         employeeMap.clear();
         loaded.forEach(emp -> employeeMap.put(emp.getId(), emp));

         System.out.println("Loaded " + employees.size() + " employee(s) from file.");
     } catch (FileNotFoundException e) {
         System.out.println("No previous data found. Starting fresh.");
     } catch (Exception e) {
         System.out.println("Error loading file: " + e.getMessage());
     }
 }

 private void saveToFile() {
     try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
         oos.writeObject(employees);
         // System.out.println("Data saved to file."); // for debug
     } catch (IOException e) {
         System.out.println("Error saving to file: " + e.getMessage());
     }
 }
}

//====================== EMPLOYEE CLASS ======================
class Employee implements Serializable {
 private static final long serialVersionUID = 1L;

 private String id;
 private String name;
 private String department;
 private double salary;

 public Employee(String id, String name, String department, double salary) {
     this.id = id;
     this.name = name;
     this.department = department;
     this.salary = salary;
 }

 // Getters
 public String getId() { return id; }
 public String getName() { return name; }
 public String getDepartment() { return department; }
 public double getSalary() { return salary; }

 // Setters (for update)
 public void setName(String name) { this.name = name; }
 public void setDepartment(String department) { this.department = department; }
 public void setSalary(double salary) { this.salary = salary; }

 @Override
 public String toString() {
     return String.format("ID: %-8s | Name: %-15s | Dept: %-12s | Salary: ₹%,.2f",
             id, name, department, salary);
 }
}

//====================== CUSTOM EXCEPTIONS ======================
class InvalidEmployeeDataException extends Exception {
 public InvalidEmployeeDataException(String message) {
     super(message);
 }
}

class EmployeeNotFoundException extends Exception {
 public EmployeeNotFoundException(String message) {
     super(message);
 }
}

class DuplicateEmployeeException extends Exception {
 public DuplicateEmployeeException(String message) {
     super(message);
 }
}
