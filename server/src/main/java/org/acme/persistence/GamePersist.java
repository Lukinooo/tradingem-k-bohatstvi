package org.acme.persistence;

import org.acme.models.Game;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class GamePersist implements PersistenceLayer{
    @Inject
    EntityManager em;

    public GamePersist(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object get(Long id) {
        return em.find(Game.class, id);
    }

    public Object getByName(String name) {
        Query query = em.createQuery("SELECT g FROM Game g WHERE g.name LIKE :name");
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public Long create(Object object) {
        Game game = (Game) object;
        em.persist(game);
        return game.getId();
    }

    @Override
    public Long delete(Object object) {
        Game game = (Game) object;
        em.remove(game);
        return game.getId();
    }

    @Override
    public Long update(Object object) {
        Game game = (Game) object;
        em.merge(game);
        return game.getId();
    }

    @Override
    public List getAll() {
        Query query = em.createQuery("SELECT g FROM Game g");
        return query.getResultList();
    }

    // Print query
//    org.hibernate.query.Query hq = query.unwrap(org.hibernate.query.Query.class);
//    System.out.println(hq.getQueryString());
}
