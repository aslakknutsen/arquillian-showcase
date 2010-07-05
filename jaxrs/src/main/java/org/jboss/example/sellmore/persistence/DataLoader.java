package org.jboss.example.sellmore.persistence;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.example.sellmore.data.Contact;
import org.jboss.example.sellmore.data.Customer;
import org.jboss.example.sellmore.data.SalesOrder;

//@Singleton
//@Startup
public class DataLoader {
   @PersistenceContext EntityManager em;

   @PostConstruct
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public void load() {
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
   }
}
