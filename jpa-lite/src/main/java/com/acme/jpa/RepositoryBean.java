package com.acme.jpa;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@Stateful
@Local(Repository.class)
public class RepositoryBean implements Repository {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    public void create(final Serializable entity) {
        em.persist(entity);
    }

    @Override
    public void delete(final Serializable entity) {
        em.remove(entity);
    }

    @Override
    public <T> T retrieveById(final Class<T> type, final Long id) {
        return (T) em.find(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> retrieveByQuery(Class<T> type, String query, String... params) {
        Query q = em.createQuery(query);
        for (int i = 0; i < params.length; i++) {
            q.setParameter(i + 1, params[i]);
        }
        return (List<T>) q.getResultList();
    }

    @Override
    public void saveNonManaged(final Serializable entity) {
        em.merge(entity);
    }

    @Override
    public boolean isManaging(final Serializable entity) {
        return em.contains(entity);
    }

    @Override
    @Remove
    public void close() {
        em.flush();
        em.clear();
        em.close();
    }

    @Override
    public void update(Serializable entity) {
        // any transaction in which the entity manager is enlisted will flush the changes
    }
}