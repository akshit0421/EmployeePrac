package com.ems;

import com.ems.dao.EmployeeDAO;
import com.ems.entity.Employee;

import com.ems.dao.DepartmentDAO;
import com.ems.entity.Department;

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
            System.out.println("\n===== Employee Management System =====");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Delete Employee");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();

                    List<Department> departments = deptDao.getAll();
                    System.out.println("Choose Department:");
                    for (Department d : departments) {
                        System.out.println(d.getId() + ". " + d.getName());
                    }
                    System.out.print("Enter department id: ");
                    int deptId = Integer.parseInt(scanner.nextLine());
                    Department dept = deptDao.getById(deptId);

                    if (dept == null) {
                        System.out.println("❌ Invalid department!");
                        break;
                    }

                    System.out.print("Enter salary: ");
                    double salary = Double.parseDouble(scanner.nextLine());

                    Employee emp = new Employee(name, salary, dept);
                    dao.saveEmployee(emp);
                    System.out.println("✅ Employee added successfully!");
                }

                case 2 -> {
                    System.out.println("\n--- Employee List ---");

                    var employees = dao.getAllEmployees();
                    if (employees.isEmpty()) {
                        System.out.println("No employees found.");
                    } else {
                        for (Employee e : employees) {
                            System.out.println("+--------------------------------------+");
                            System.out.println("| ID         : " + e.getId());
                            System.out.println("| Name       : " + e.getName());
                            System.out.println("| Department : " + e.getDepartment());
                            System.out.println("| Salary     : " + e.getSalary());
                            System.out.println("+--------------------------------------+\n");
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter Employee ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    dao.deleteEmployee(id);
                    System.out.println("✅ Employee deleted (if existed).");
                }
                case 4 -> {
                    System.out.println("👋 Exiting... Goodbye!");
                    scanner.close();
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
