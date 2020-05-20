package org.acme.persistence;

import org.acme.models.Game;
import org.acme.models.Product;
import org.acme.models.Shop;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ProductPersistence implements PersistenceLayer{
    @Inject
    EntityManager em;

    public ProductPersistence(EntityManager em) {
        this.em = em;
    }

    @Override
    public Object get(Long id) {
        return em.find(Product.class, id);
    }

    @Override
    public Long create(Object object) {
        Product product = (Product) object;
        em.persist(product);
        return product.getId();
    }

    @Override
    public Long delete(Object object) {
        Product product = (Product) object;
        em.remove(product);
        return product.getId();
    }

    @Override
    public Long update(Object object) {
        Product product = (Product) object;
        em.merge(product);
        return product.getId();
    }

    /**
     * Function gets all products stored in the database
     * @return all products from the database
     */
    @Override
    public List<Object> getAll() {
        Query query = em.createQuery("SELECT p FROM Product p");
        return query.getResultList();
    }
}
