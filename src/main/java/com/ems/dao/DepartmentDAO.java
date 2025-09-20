package com.ems.dao;

import com.ems.entity.Department;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentDAO {

    private final SessionFactory sessionFactory;

    // Spring will automatically inject the SessionFactory bean
    public DepartmentDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Department dept) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(dept);
            tx.commit();
        }
    }

    public List<Department> getAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Department d order by d.id", Department.class).list();
        }
    }

    public Department getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Department.class, id);
        }
    }

    public void createDepartment(String name) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Department dept = new Department(name);
            session.persist(dept);
            tx.commit();
            System.out.println("✅ Department '" + name + "' created with ID: " + dept.getId());
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void deleteDepartment(int id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            Department dept = session.get(Department.class, id);
            if (dept == null) return;

            tx = session.beginTransaction();

            // Delete all employees in this department first
            session.createQuery("delete from Employee e where e.department.id = :deptId")
                    .setParameter("deptId", id)
                    .executeUpdate();

            // Now delete department
            session.remove(dept);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    public void printDepartments() {
        try (Session session = sessionFactory.openSession()) {
            List<Department> departments =
                    session.createQuery("from Department", Department.class).list();

            System.out.println("\nID | Department Name | Employee Count");
            System.out.println("-------------------------------------");

            for (Department dept : departments) {
                System.out.println(dept.getId() + " | "
                        + dept.getName() + " | "
                        + dept.getEmployeeCount());
            }
        }
    }
}
