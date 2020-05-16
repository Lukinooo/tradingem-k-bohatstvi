package org.acme.persistence;

import org.acme.models.Game;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class GamePersist implements PersistenceLayer{
    @Inject
    EntityManager em;

    public GamePersist(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Object get(Long id) {
        return em.find(Game.class, id);
    }

    @Override
    @Transactional
    public Long create(Object object) {
        Game game = (Game) object;
        System.out.println(game.getColor());
        em.persist(game);
        return game.getId();
    }

    @Override
    @Transactional
    public Long delete(Object object) {
        Game game = (Game) object;
        em.remove(game);
        return game.getId();
    }

    @Override
    @Transactional
    public Long update(Object object) {
        Game game = (Game) object;
        em.merge(game);
        return game.getId();
    }

}
