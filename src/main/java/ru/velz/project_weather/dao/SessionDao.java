package ru.velz.project_weather.dao;

import lombok.AllArgsConstructor;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.model.Session;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SessionDao {
    private final SessionFactory sessionFactory;

    @Transactional
    public void create(Session session) {
        org.hibernate.Session currentSession = sessionFactory.getCurrentSession();
        currentSession.persist(session);

    }

    @Transactional
    public Optional<Session> findSessionById(UUID uuid) {
        org.hibernate.Session currentSession = sessionFactory.getCurrentSession();
        Query<Session> query = currentSession.createQuery("FROM Session WHERE id = :uuid", Session.class);
        query.setParameter("uuid", uuid);
        return query.uniqueResultOptional();
    }
}
