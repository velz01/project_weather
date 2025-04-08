package ru.velz.project_weather.dao;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.model.User;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserDao {
    private final SessionFactory sessionFactory;

    @Transactional
    public void create(User user) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(user);
    }

    @Transactional
    public Optional<User> findUserByLogin(String login) {
        Session currentSession = sessionFactory.getCurrentSession();
        Query<User> query = currentSession.createQuery("FROM User WHERE login = :login", User.class);
        query.setParameter("login", login);
        return query.uniqueResultOptional();
    }
}
