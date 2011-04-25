package com.acme.jaxrs.persistence;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.Disposes;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {
    // NOTE cannot use producer field because Weld attempts to close EntityManager
    // https://jira.jboss.org/jira/browse/WELD-341
    @PersistenceContext
    EntityManager em;

    public @Produces
    EntityManager retrieveEntityManager() {
        return em;
    }

    public void disposeEntityManager(@Disposes EntityManager em) {
    }
}