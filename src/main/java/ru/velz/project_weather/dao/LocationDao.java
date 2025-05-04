package ru.velz.project_weather.dao;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.velz.project_weather.model.Location;

import java.util.List;

@AllArgsConstructor
@Component
public class LocationDao {
    private final SessionFactory sessionFactory;



    @Transactional
    public void create(Location location) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.persist(location);
    }

    @Transactional
    public List<Location> findLocationsByUserId(Long id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.createQuery("FROM Location WHERE user.id = :id", Location.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public void delete(Long locationId) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.createMutationQuery("DELETE FROM Location WHERE id = :id")
                .setParameter("id", locationId)
                .executeUpdate();
    }
}
