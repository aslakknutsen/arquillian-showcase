package com.acme.jpa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateless
@Local(PersistenceService.class)
public class PersistenceServiceBean implements PersistenceService {
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;

    @Override
    public List<Record> seed(boolean clear) {
        if (clear) {
            em.createQuery("delete from Record").executeUpdate();
        }
        List<Record> records = new ArrayList<Record>();
        Record a = new Record("Record A");
        LineItem l1 = new LineItem(new BigDecimal(50));
        a.addLineItem(l1);
        em.persist(a);
        records.add(a);
        return records;
    }

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

    @Override
    public void transact() {
    }

    public String getProvider() {
        return em.getDelegate().getClass().getName();
    }

    public boolean isLazyLoadingPermittedOnClosedSession() {
        return "org.eclipse.persistence.internal.jpa.EntityManagerImpl".equals(getProvider()) ? true : false;
    }

}
