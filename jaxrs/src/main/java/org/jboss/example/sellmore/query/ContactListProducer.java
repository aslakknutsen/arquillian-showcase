package org.jboss.example.sellmore.query;

import org.jboss.example.sellmore.data.Customer;
import org.jboss.example.sellmore.data.Contact;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class ContactListProducer {
   @Inject EntityManager em;
   @Inject @Selected Customer selectedCustomer;
   private List<Contact> contacts;

   @Produces @Named public List<Contact> getContacts() {
      return contacts;
   }

   @PostConstruct
   public void fetch() {
      contacts = em.createQuery("select c from Contact c where c.customer.id = :customerId order by c.city")
         .setParameter("customerId", selectedCustomer.getId())
         .getResultList();
   }
}
