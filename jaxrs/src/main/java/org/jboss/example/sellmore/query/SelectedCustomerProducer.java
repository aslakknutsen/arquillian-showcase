package org.jboss.example.sellmore.query;

import org.jboss.example.sellmore.data.Customer;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Model
public class SelectedCustomerProducer {

   @Inject EntityManager em;
   private Long customerId;
   private Customer customer;

   public Long getCustomerId()
   {
      return customerId;
   }

   public void setCustomerId(Long customerId)
   {
      this.customerId = customerId;
      // we have to call this here so that it's available for binding form params
      lookup();
   }

   @Produces @Selected @Named Customer getSelectedCustomer() {
      return customer;
   }

   public void lookup() {
      if (customerId != null) {
         customer = em.find(Customer.class, customerId);
      }
      if (customer == null) {
         FacesContext ctx = FacesContext.getCurrentInstance();
         ctx.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid customer", "Invalid customer"));
         // not working
         ctx.getExternalContext().getFlash().setKeepMessages(true);
         ctx.getApplication().getNavigationHandler().handleNavigation(ctx, null, "/customers.xhtml?faces-redirect=true");
         ctx.responseComplete();
      }
   }
}
