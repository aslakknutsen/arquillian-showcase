package org.jboss.example.sellmore.action;

import org.jboss.example.sellmore.data.Customer;
import java.io.Serializable;
import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

//@Stateful
@Named
@ConversationScoped
public class CustomerEditor
      implements Serializable // NOTE implement Serializable for SE tests
{
   @Inject EntityManager em;

   private Customer customer;

   @Produces @Named
   public Customer getCustomer() {
      if (customer == null) {
         initNewCustomer();
      }
      return customer;
   }

   public void add() {
      em.persist(customer);
   }

   protected void initNewCustomer() {
      Customer c = new Customer();
      customer = c;
   }
}
