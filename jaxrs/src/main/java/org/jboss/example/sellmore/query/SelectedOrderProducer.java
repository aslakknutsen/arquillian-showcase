package org.jboss.example.sellmore.query;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.example.sellmore.data.SalesOrder;

@Model
public class SelectedOrderProducer {

   @Inject EntityManager em;
   private Long orderId;
   private SalesOrder order;

   public Long getOrderId()
   {
      return orderId;
   }

   public void setOrderId(Long orderId)
   {
      this.orderId = orderId;
   }

   @Produces @Selected @Named SalesOrder getSelectedOrder() {
      return order;
   }

   public void lookup() {
      if (orderId != null) {
         order = em.find(SalesOrder.class, orderId);
      }
      if (order == null) {
         FacesContext ctx = FacesContext.getCurrentInstance();
         ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid order", "Invalid order"));
         // not working
         ctx.getExternalContext().getFlash().setKeepMessages(true);
         ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, "/customers.xhtml?faces-redirect=true");
         ctx.responseComplete();
      }
   }
}
