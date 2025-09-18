package com.ems;

import com.ems.dao.EmployeeDAO;
import com.ems.dao.ProjectDAO;
import com.ems.entity.Employee;

import com.ems.dao.DepartmentDAO;
import com.ems.entity.Department;
import com.ems.entity.Project;

import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        EmployeeDAO dao = new EmployeeDAO();
        DepartmentDAO deptDao = new DepartmentDAO();

        String[] defaultDepts = {"Finance", "HR", "IT", "Marketing", "Operations"};
        for (String d : defaultDepts) {
            deptDao.save(new Department(d));
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Employee Management");
            System.out.println("2. Department Management");
            System.out.println("3. Project Management");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int mainChoice = Integer.parseInt(scanner.nextLine());

            switch (mainChoice) {
                case 1 -> employeeMenu(scanner, dao, deptDao);
                case 2 -> departmentMenu(scanner, deptDao);
                case 3 -> projectMenu(scanner, new ProjectDAO());
                case 4 -> System.exit(0);
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    private static void departmentMenu(Scanner scanner, DepartmentDAO departmentDAO) {
        while (true) {
            System.out.println("\n===== Department Management =====");
            System.out.println("1. View Departments");
            System.out.println("2. Add Department");
            System.out.println("3. Delete Department");
            System.out.println("4. Employee Count In Department");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    List<Department> departments = departmentDAO.getAll();
                    if (departments.isEmpty()) {
                        System.out.println("No departments found.");
                    } else {
                        departments.forEach(d -> System.out.println(d.getId() + ". " + d.getName()));
                    }
                }
                case 2 -> {
                    System.out.print("Enter new department name: ");
                    String name = scanner.nextLine();
                    if (!name.isBlank()) {
                        departmentDAO.createDepartment(name);
                    } else {
                        System.out.println("❌ Invalid name.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Department ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    departmentDAO.deleteDepartment(id);
                }
                case 4 -> departmentDAO.printDepartments();
                case 5 -> {
                    return; // Back to main menu
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }


    private static void employeeMenu(Scanner scanner, EmployeeDAO employeeDAO, DepartmentDAO departmentDAO) {
        while (true) {
            System.out.println("\n===== Employee Management =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    // Add Employee
                    System.out.print("Enter Employee Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Salary: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    // Show departments
                    List<Department> departments = departmentDAO.getAll();
                    if (departments.isEmpty()) {
                        System.out.println("No departments found. Please add a department first.");
                        break;
                    }
                    System.out.println("Available Departments:");
                    departments.forEach(d -> System.out.println(d.getId() + ". " + d.getName()));
                    System.out.print("Enter Department ID: ");
                    int deptId = Integer.parseInt(scanner.nextLine());
                    Department dept = departmentDAO.getById(deptId);

                    Employee emp = new Employee();
                    emp.setName(name);
                    emp.setSalary(salary);
                    emp.setDepartment(dept);

                    employeeDAO.saveEmployee(emp);
                }

                case 2 -> {
                    // View Employees
                    List<Employee> employees = employeeDAO.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees found.");
                    } else {
                        System.out.println("\nID | Name | Salary | Department");
                        employees.forEach(e ->
                                System.out.println(e.getId() + " | " + e.getName() + " | " + e.getSalary() + " | " +
                                        (e.getDepartment() != null ? e.getDepartment().getName() : "None"))
                        );
                    }
                }

                case 3 -> {
                    // Update Employee
                    System.out.print("Enter Employee ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter new name (or leave blank to keep): ");
                    String newName = scanner.nextLine();
                    if (newName.isBlank()) newName = null;

                    System.out.print("Enter new salary (or leave blank to keep): ");
                    String salaryInput = scanner.nextLine();
                    Double newSalary = salaryInput.isBlank() ? null : Double.parseDouble(salaryInput);

                    // Show departments
                    List<Department> departments = departmentDAO.getAll();
                    if (departments.isEmpty()) {
                        System.out.println("No departments found. Please add a department first.");
                        break;
                    }
                    System.out.println("Available Departments:");
                    departments.forEach(d -> System.out.println(d.getId() + ". " + d.getName()));
                    System.out.print("Enter new Department ID (or leave blank to keep): ");
                    String deptInput = scanner.nextLine();
                    Department newDept = deptInput.isBlank() ? null : departmentDAO.getById(Integer.parseInt(deptInput));

                    // Call DAO update method
                    employeeDAO.updateEmployee(id, newName, newSalary, newDept);
                }

                case 4 -> {
                    // Delete Employee
                    System.out.print("Enter Employee ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    employeeDAO.deleteEmployee(id);
                }

                case 5 -> {
                    return; // Back to Main Menu
                }

                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

    private static void projectMenu(Scanner scanner, ProjectDAO projectDAO) {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        while (true) {
            System.out.println("\n===== Project Management =====");
            System.out.println("1. View Projects");
            System.out.println("2. Add Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Assign Project to Employee");
            System.out.println("5. View Employees with Projects");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    var projects = projectDAO.getAll();
                    if (projects.isEmpty()) {
                        System.out.println("No projects found.");
                    } else {
                        projects.forEach(p -> System.out.println(p.getId() + ". " + p.getName()));
                    }
                }
                case 2 -> {
                    System.out.print("Enter new project name: ");
                    String name = scanner.nextLine();
                    if (!name.isBlank()) {
                        projectDAO.saveProject(new Project(name));
                    } else {
                        System.out.println("❌ Invalid name.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Project ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    projectDAO.deleteProject(id);
                }
                case 4 -> {
                    // Assign project to employee
                    var employees = employeeDAO.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees available. Add employees first.");
                        break;
                    }
                    var projects = projectDAO.getAll();
                    if (projects.isEmpty()) {
                        System.out.println("No projects available. Add projects first.");
                        break;
                    }

                    System.out.println("Available Employees:");
                    employees.forEach(e -> System.out.println(e.getId() + ". " + e.getName()));

                    System.out.print("Enter Employee ID: ");
                    int empId = Integer.parseInt(scanner.nextLine());
                    Employee emp = employeeDAO.getEmployeeById(empId);

                    System.out.println("Available Projects:");
                    projects.forEach(p -> System.out.println(p.getId() + ". " + p.getName()));

                    System.out.print("Enter Project ID: ");
                    int projId = Integer.parseInt(scanner.nextLine());
                    Project project = projectDAO.getById(projId);

                    if (emp != null && project != null) {
                        employeeDAO.assignProject(empId, projId);
                        System.out.println("✅ Project assigned successfully.");
                    } else {
                        System.out.println("❌ Invalid employee or project.");
                    }
                }
                case 5 -> {
                    var employees = employeeDAO.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees found.");
                    } else {
                        employees.forEach(e -> {
                            System.out.print("Employee: " + e.getName() + " → Projects: ");
                            if (e.getProjects().isEmpty()) {
                                System.out.println("None");
                            } else {
                                e.getProjects().forEach(p -> System.out.print(p.getName() + " "));
                                System.out.println();
                            }
                        });
                    }
                }
                case 6 -> {
                    return; // Back to main menu
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }

}
