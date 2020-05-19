package org.acme.persistence;

import org.acme.models.Game;
import org.acme.models.Shop;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Queue;

public class ShopPersist implements PersistenceLayer{
    @Inject
    EntityManager em;

    public ShopPersist(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object get(Long id) {
        return em.find(Shop.class, id);
    }

    @Override
    public Long create(Object object) {
        Shop shop = (Shop) object;
        em.persist(shop);
        return shop.getId();
    }

    @Override
    public Long delete(Object object) {
        Shop shop = (Shop) object;
        em.remove(shop);
        return shop.getId();
    }

    @Override
    public Long update(Object object) {
        Shop shop = (Shop) object;
        em.merge(shop);
        return shop.getId();
    }

    @Override
    public List<Object> getAll() {
        Query query = em.createQuery("SELECT s FROM Shop s");
        return query.getResultList();
    }

    public List<Shop> getAllById(Game game) {
        Query query = em.createQuery("SELECT s FROM Shop s WHERE s.game = " + game.getId());
        return query.getResultList();
    }

    public List<Shop> getAllById(Long gameId) {
        Query query = em.createQuery("SELECT s FROM Shop s WHERE s.game = " + gameId);
        return query.getResultList();
    }
}
