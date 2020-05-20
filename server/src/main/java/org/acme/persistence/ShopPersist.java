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

    /**
     * Function gets all shops from the database
     * @return all shops from the database
     */
    @Override
    public List<Object> getAll() {
        Query query = em.createQuery("SELECT s FROM Shop s");
        return query.getResultList();
    }

    /**
     * Function gets all shops which game id is equal to given
     * @param game object which should be referred in selected games
     * @return all games which have equal game id as given
     */
    public List<Shop> getAllById(Game game) {
        Query query = em.createQuery("SELECT s FROM Shop s WHERE s.game = " + game.getId());
        return query.getResultList();
    }

    /**
     * Function gets all shops which game id is equal to given but the param is only long, not object
     * @param gameId long number which represents game id
     * @return all games which have equal game id as given
     */
    public List<Shop> getAllById(Long gameId) {
//        Query query = em.createQuery("SELECT s.id as id, s.name as name, s.latitude as latitude, s.longitude as longitude FROM Shop s WHERE s.game = " + gameId);
        Query query = em.createQuery("SELECT s FROM Shop s WHERE s.game = " + gameId);
        return query.getResultList();
    }
}
