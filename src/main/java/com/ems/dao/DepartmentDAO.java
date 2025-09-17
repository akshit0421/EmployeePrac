package com.ems.dao;

import com.ems.entity.Department;
import com.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class DepartmentDAO {

    public void save(Department dept) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(dept);
            tx.commit();
        }
    }

    public List<Department> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Department d order by d.id", Department.class).list();
        }
    }

    public Department getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Department.class, id);
        }
    }

    public void createDepartment(String name) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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

    // In DepartmentDAO
    public void deleteDepartment(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
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



}
