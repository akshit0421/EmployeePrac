package com.ems;

import com.ems.dao.DepartmentDAO;
import com.ems.dao.EmployeeDAO;
import com.ems.dao.ProjectDAO;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.ems.dao") // scans your DAOs
public class AppConfig {

    @Bean
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml") // your Hibernate XML
                .buildSessionFactory();
    }

    @Bean
    public EmployeeDAO employeeDAO(SessionFactory sessionFactory) {
        return new EmployeeDAO(sessionFactory);
    }

    @Bean
    public DepartmentDAO departmentDAO(SessionFactory sessionFactory) {
        return new DepartmentDAO(sessionFactory);
    }

    @Bean
    public ProjectDAO projectDAO(SessionFactory sessionFactory) {
        return new ProjectDAO(sessionFactory);
    }
}
