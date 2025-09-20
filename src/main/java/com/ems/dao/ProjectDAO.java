package com.ems.dao;

import com.ems.entity.Project;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository   // Marks this as a Spring bean
public class ProjectDAO {

    private final SessionFactory sessionFactory;

    // SessionFactory is injected by Spring from AppConfig
    public ProjectDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    // Save a new project
    public void saveProject(Project project) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Project", Project.class).list();
        }
    }

    // Get project by ID
    public Project getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Project.class, id);
        }
    }

    // Delete project by ID
    public void deleteProject(int id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
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
