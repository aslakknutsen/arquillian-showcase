package org.jboss.example.sellmore.query;

import org.jboss.example.sellmore.data.Customer;
import org.jboss.example.sellmore.data.SalesOrder;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class OrderListProducer {
   @Inject EntityManager em;
   @Inject @Selected Customer selectedCustomer;
   private List<SalesOrder> orders;

   @Produces @Named public List<SalesOrder> getOrders() {
      return orders;
   }

   @PostConstruct
   public void fetch() {
      orders = em.createQuery("select o from SalesOrder o where o.customer.id = :customerId order by o.created")
         .setParameter("customerId", selectedCustomer.getId())
         .getResultList();
   }
}
