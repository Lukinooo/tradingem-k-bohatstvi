package org.acme.persistence;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public interface PersistenceLayer {
    @Inject
    EntityManager em = null;

    public Object get(Long id);

    public Long create(Object object);

    public Long delete(Object object);

    public Long update(Object object);

    public List<Object> getAll();

}
