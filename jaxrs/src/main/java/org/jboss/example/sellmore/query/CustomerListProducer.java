package org.jboss.example.sellmore.query;

import org.jboss.example.sellmore.data.Customer;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class CustomerListProducer
{
   @Inject EntityManager em;

   private List<Customer> customers;

   @Produces @Named
   public List<Customer> getCustomers() {
      return customers;
   }

   public void update(@Observes(notifyObserver = Reception.IF_EXISTS) Customer item) {
      fetch();
   }

   @PostConstruct
   public void fetch() {
      customers = em.createQuery("select c from Customer c order by c.name").getResultList();
   }
}
