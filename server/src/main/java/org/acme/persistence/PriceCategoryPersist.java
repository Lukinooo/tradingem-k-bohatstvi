package org.acme.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class PriceCategoryPersist implements PersistenceLayer {
    @Inject
    EntityManager em;

    public PriceCategoryPersist(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object get(Long id) {
        return null;
    }

    @Override
    public Long create(Object object) {
        return null;
    }

    @Override
    public Long delete(Object object) {
        return null;
    }

    @Override
    public Long update(Object object) {
        return null;
    }

    @Override
    public List<Object> getAll() {
        Query query = em.createQuery("SELECT pc FROM PriceCategory pc");
        return query.getResultList();
    }
}
