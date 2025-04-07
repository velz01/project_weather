package ru.velz.project_weather.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.model.User;

@Component
public class RegistrationDao {
    private final SessionFactory sessionFactory;

    @Autowired
    public RegistrationDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public void createUser(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
        System.out.println(user);
    }
}
