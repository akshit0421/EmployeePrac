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
            return session.createQuery("from Department", Department.class).list();
        }
    }

    public Department getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Department.class, id);
        }
    }
}
