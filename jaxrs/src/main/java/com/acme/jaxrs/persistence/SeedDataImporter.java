package com.acme.jaxrs.persistence;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.acme.jaxrs.model.Contact;
import com.acme.jaxrs.model.Customer;
import com.acme.jaxrs.model.SalesOrder;

@Singleton
@Startup
public class SeedDataImporter {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserTransaction utx;

    @PostConstruct
    public void load() throws Exception {
        utx.begin();
        em.joinTransaction();
        Customer acme = new Customer("Acme Corporation");
        em.persist(acme);
        em.persist(new Customer("Starbucks"));
        Contact contact = new Contact();
        contact.setAddress("100 Broadway");
        contact.setCity("New York");
        contact.setPhone("212-555-1212");
        contact.setCustomer(acme);
        em.persist(contact);
        SalesOrder order = new SalesOrder();
        order.setCreated(new Date());
        order.setAmount(new BigDecimal(100));
        order.setCustomer(acme);
        em.persist(order);
        utx.commit();
    }
}
