package com.ems.dao;

import com.ems.entity.Employee;
import com.ems.entity.Department;
import com.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeDAO {

    // Save employee
    public void saveEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(employee);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Fetch all employees
    public List<Employee> getAllEmployees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Employee", Employee.class).list();
        }
    }

    // Fetch by ID
    public Employee getEmployeeById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    // Update employee
    public void updateEmployee(int id, String newName, Double newSalary, Department newDept) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Step 1: fetch the employee using existing method
            Employee emp = getEmployeeById(id); // uses your DAO's getEmployeeById
            if (emp == null) {
                System.out.println("❌ Employee not found!");
                return;
            }

            tx = session.beginTransaction();

            // Step 2: update fields if provided
            if (newName != null && !newName.isBlank()) emp.setName(newName);
            if (newSalary != null) emp.setSalary(newSalary);
            if (newDept != null) emp.setDepartment(newDept);

            // Step 3: save changes
            session.merge(emp);
            tx.commit();
            System.out.println("✅ Employee updated successfully!");
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }


    // Delete employee
    public void deleteEmployee(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) session.remove(emp);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}