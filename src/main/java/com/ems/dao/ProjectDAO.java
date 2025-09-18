package com.ems.dao;

import com.ems.entity.Project;
import com.ems.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ProjectDAO {

    // Save a new project
    public void saveProject(Project project) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(project);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Get all projects
    public List<Project> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        }
    }

    // Get project by ID
    public Project getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Project.class, id);
        }
    }

    // Delete project by ID
    public void deleteProject(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Project project = session.get(Project.class, id);
            if (project != null) {
                session.remove(project);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
