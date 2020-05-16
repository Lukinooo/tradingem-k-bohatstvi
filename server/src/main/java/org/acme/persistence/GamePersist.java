package org.acme.persistence;

import org.acme.models.Game;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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

    @Override
    public Long create(Object object) {
        Game game = (Game) object;
        System.out.println(game.getColor());
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
    public List<Object> getAll()
    {return null;}
}
