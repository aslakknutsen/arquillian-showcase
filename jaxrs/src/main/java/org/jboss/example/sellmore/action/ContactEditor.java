package org.jboss.example.sellmore.action;

import org.jboss.example.sellmore.data.Customer;
import org.jboss.example.sellmore.data.Contact;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import org.jboss.example.sellmore.query.Selected;

//@Stateful
@Named
@ConversationScoped
public class ContactEditor 
      implements Serializable // NOTE implement Serializable for SE tests
{
   @Inject EntityManager em;

   @Inject @Selected Customer customer;
   
   private Contact contact;

   @Produces @Named
   public Contact getContact() {
      if (contact == null) {
         initNewContact();
      }
      return contact;
   }

   public void add() {
      // this should really be set when the editor is loaded
      contact.setCustomer(customer);
      em.persist(contact);
   }

   protected void initNewContact() {
      Contact c = new Contact();
      contact = c;
   }
}
