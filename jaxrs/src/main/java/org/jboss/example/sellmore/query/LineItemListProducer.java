package org.jboss.example.sellmore.query;

import org.jboss.example.sellmore.data.SalesOrder;
import org.jboss.example.sellmore.data.LineItem;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@RequestScoped
public class LineItemListProducer {
   @Inject EntityManager em;
   @Inject @Selected SalesOrder order;
   private List<LineItem> lineItems;

   @Produces @Named public List<LineItem> getLineItems() {
      return lineItems;
   }

   @PostConstruct
   public void fetch() {
      lineItems = em.createQuery("select l from LineItem l where l.order.id = :orderId order by l.id")
         .setParameter("orderId", order.getId())
         .getResultList();
   }
}
