package com.ems.dao;

import com.ems.entity.Department;
import com.ems.entity.Employee;
import com.ems.entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository   // Marks this DAO as a Spring bean
public class EmployeeDAO {

    private final SessionFactory sessionFactory;

    // Spring will inject the SessionFactory bean defined in AppConfig
    public EmployeeDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save employee
    public void saveEmployee(Employee employee) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();

            session.persist(employee);

            // Increment department's employee count
            Department dept = employee.getDepartment();
            if (dept != null) {
                dept.setEmployeeCount(dept.getEmployeeCount() + 1);
                session.merge(dept);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Fetch all employees with their projects (JOIN FETCH)
    public List<Employee> getAllEmployees() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "SELECT DISTINCT e FROM Employee e " +
                            "LEFT JOIN FETCH e.projects " +
                            "LEFT JOIN FETCH e.department",
                    Employee.class
            ).list();
        }
    }

    // Fetch by ID with projects (JOIN FETCH)
    public Employee getEmployeeById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "SELECT e FROM Employee e " +
                                    "LEFT JOIN FETCH e.projects " +
                                    "LEFT JOIN FETCH e.department " +
                                    "WHERE e.id = :id",
                            Employee.class
                    )
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }

    // Update employee
    public void updateEmployee(int id, String newName, Double newSalary, Department newDept) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            Employee emp = getEmployeeById(id); // fetch with joins
            if (emp == null) {
                System.out.println("❌ Employee not found!");
                return;
            }

            tx = session.beginTransaction();

            Department oldDept = emp.getDepartment(); // store old department

            // Update fields
            if (newName != null && !newName.isBlank()) emp.setName(newName);
            if (newSalary != null) emp.setSalary(newSalary);

            // Update department if changed
            if (newDept != null && !newDept.equals(oldDept)) {
                emp.setDepartment(newDept);

                // Decrement old department employee count
                if (oldDept != null) {
                    oldDept.setEmployeeCount(oldDept.getEmployeeCount() - 1);
                    session.merge(oldDept);
                }

                // Increment new department employee count
                newDept.setEmployeeCount(newDept.getEmployeeCount() + 1);
                session.merge(newDept);
            }

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
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, id);
            if (emp != null) {
                Department dept = emp.getDepartment();
                session.remove(emp);

                if (dept != null) {
                    dept.setEmployeeCount(dept.getEmployeeCount() - 1);
                    session.merge(dept);
                }
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Assign project to employee
    public void assignProject(int empId, int projId) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Employee emp = session.get(Employee.class, empId);
            Project project = session.get(Project.class, projId);

            if (emp != null && project != null) {
                emp.getProjects().add(project);
                project.getEmployees().add(emp); // keep both sides in sync
                session.merge(emp);
                session.merge(project);
                System.out.println("✅ Project assigned to employee successfully!");
            } else {
                System.out.println("❌ Employee or Project not found!");
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
