package com.acme.jpa;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateful
@Local(PersistenceManager.class)
public class PersistenceManagerBean implements PersistenceManager {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager em;

    @Override
    @SuppressWarnings("unchecked")
    public List<Record> selectAll() {
        return em.createQuery("select r from Record r").getResultList();
    }

    @Override
    public Record select(Long id, boolean fetchLineItems) {
        Record r = em.find(Record.class, id);
        if (fetchLineItems) {
            r.getLineItems().size();
        }
        return r;
    }

    @Override
    public boolean isManaged(Record record) {
        return em.contains(record);
    }

}
