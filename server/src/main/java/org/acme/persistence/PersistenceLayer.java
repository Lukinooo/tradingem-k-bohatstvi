package org.acme.persistence;

import javax.transaction.Transactional;

public interface PersistenceLayer {

    @Transactional
    public Object get(Long id);

    @Transactional
    public Long create(Object object);

    @Transactional
    public Long delete(Object object);

    @Transactional
    public Long update(Object object);
}
