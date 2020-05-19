package org.acme.persistence;

import org.acme.models.Player;
import org.acme.models.Product;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class PlayerPersist implements PersistenceLayer{
    @Inject
    EntityManager em;

    public PlayerPersist(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object get(Long id) {
        return em.find(Player.class, id);
    }

    @Override
    public Long create(Object object) {
        Player player = (Player) object;
        em.persist(player);
        return player.getId();
    }

    @Override
    public Long delete(Object object) {
        Player player = (Player) object;
        em.remove(player);
        return player.getId();
    }

    @Override
    public Long update(Object object) {
        Player player = (Player) object;
        em.merge(player);
        return player.getId();
    }

    @Override
    public List<Object> getAll() {
        Query query = em.createQuery("SELECT p FROM Player p JOIN p.shops");
        return query.getResultList();
    }
}
